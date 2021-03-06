package edu.bluejack21_2.guk.controller;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import edu.bluejack21_2.guk.R;
import edu.bluejack21_2.guk.adapter.DonationAdapter;
import edu.bluejack21_2.guk.model.Dog;
import edu.bluejack21_2.guk.model.Donation;
import edu.bluejack21_2.guk.model.Notification;
import edu.bluejack21_2.guk.model.Story;
import edu.bluejack21_2.guk.model.User;
import edu.bluejack21_2.guk.util.ActivityHelper;
import edu.bluejack21_2.guk.util.Database;

public class DonationController {
    public static void showAllDonations(DonationAdapter donationAdapter, ArrayList<Donation> donations){
        Database.getDB().collection(Donation.COLLECTION_NAME)
//                .orderBy("status", Query.Direction.ASCENDING)
                .orderBy("createdAt", Query.Direction.DESCENDING)
//                .whereEqualTo("status", "pending")
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()){
                    Donation donation = document.toObject(Donation.class);
                    donation.setId(document.getId());
                    donations.add(donation);
                    donationAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static void showAllDonationsByUser(DonationAdapter donationAdapter, ArrayList<Donation> donations){
        DocumentReference userRef = Database.getDB().collection(User.COLLECTION_NAME).document(User.CURRENT_USER.getId());

        Database.getDB().collection(Donation.COLLECTION_NAME)
                .whereEqualTo("user", userRef)
//                .orderBy("status", Query.Direction.ASCENDING)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for (QueryDocumentSnapshot document : task.getResult()){
                    Donation donation = document.toObject(Donation.class);
                    donation.setId(document.getId());
                    donations.add(donation);
                    donationAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    
    public static void changeDonationStatus(Context ctx, Donation donation, boolean isApproved){
        HashMap<String, Object> data =  new HashMap<String, Object>();
        data.put("status", isApproved ? 1 : 2);
        Database.getDB().collection(Donation.COLLECTION_NAME).document(donation.getId()).set(data, SetOptions.merge()).addOnSuccessListener(u -> {
//            String notif = "";
            int status = 0;
            if(isApproved){
//                notif = "Your donation has been approved!";
                status = 1;

//                data.clear();
                int point = (int)(Math.ceil (donation.getAmount() / 100000.0 * 100.0));
//                data.put("point", FieldValue.increment(point));
//                donation.getUser().update(data).addOnSuccessListener(unused -> {
//                });
                UserController.increasePoint(donation.getUser(), point);

                ActivityHelper.refreshActivity((Activity) ctx);
                Toast.makeText(ctx, ctx.getString(R.string.donation_approved), Toast.LENGTH_LONG).show();
            } else {
//                notif = "Your donation has been rejected!";
                status = 2;

                ActivityHelper.refreshActivity((Activity) ctx);
                Toast.makeText(ctx, ctx.getString(R.string.donation_rejected), Toast.LENGTH_LONG).show();
            }
            NotificationController.insertNotification(status, "donation", donation.getUser());

        });
    }

    public static boolean insertDonation(Context ctx, String bankAccountHolder, String bankAccountNumber, String amountStr, String notes, Uri filePath){
        String errorMsg = "";

        int amount = 0;
        if(bankAccountHolder.isEmpty()){
            errorMsg = ctx.getString(R.string.name_error_msg);
        } else if(bankAccountNumber.isEmpty()){
            errorMsg = ctx.getString(R.string.bank_number_error_msg);
        } else if(amountStr.isEmpty()) {
            errorMsg = ctx.getString(R.string.donation_amount_error_msg);
        } else if(filePath == null){
            errorMsg = ctx.getString(R.string.picture_error_msg);
        } else {
            try{
                amount = Integer.parseInt(amountStr);
            } catch (Exception e){
                errorMsg = ctx.getString(R.string.donation_amount_error_msg);
            }
        }



        if(!errorMsg.isEmpty()){
            Toast.makeText(ctx, errorMsg, Toast.LENGTH_SHORT).show();
            return false;
        }

        String extension = filePath.toString().substring(filePath.toString().lastIndexOf(".") + 1);
        String fileName = "images/donations/" + UUID.randomUUID().toString() + "." + extension;


        Database.uploadImage(filePath, fileName, ctx, (data, message) -> {
            DocumentReference userRef = Database.getDB().collection(User.COLLECTION_NAME).document(User.CURRENT_USER.getId());
            Donation donation = new Donation(bankAccountHolder, bankAccountNumber, Integer.parseInt(amountStr), notes, data, userRef, Timestamp.now());
            Database.getDB().collection(Donation.COLLECTION_NAME).add(donation.toMap()).addOnSuccessListener(documentReference -> {
                ActivityHelper.refreshActivity((Activity) ctx);
                Toast.makeText(ctx, ctx.getString(R.string.donation_insert), Toast.LENGTH_LONG).show();

            }).addOnFailureListener(e -> {
                Toast.makeText(ctx, "Error!", Toast.LENGTH_SHORT).show();
            });
        });
        return true;
    }
}
