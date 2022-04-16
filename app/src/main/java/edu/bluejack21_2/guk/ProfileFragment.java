package edu.bluejack21_2.guk;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.bluejack21_2.guk.adapter.AdoptionAdapter;
import edu.bluejack21_2.guk.adapter.DonationAdapter;
import edu.bluejack21_2.guk.controller.AdoptionController;
import edu.bluejack21_2.guk.controller.DonationController;
import edu.bluejack21_2.guk.controller.UserController;
import edu.bluejack21_2.guk.model.Adoption;
import edu.bluejack21_2.guk.model.Donation;
import edu.bluejack21_2.guk.model.User;
import edu.bluejack21_2.guk.util.Database;

public class ProfileFragment extends Fragment {

    private TextView nameTxt, pointTxt;
    private ImageView profilePic, profileBadge;
    private Button logoutBtn, deleteAccountBtn;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameTxt = view.findViewById(R.id.profile_user_name);
        pointTxt = view.findViewById(R.id.profile_user_points);
        profilePic = view.findViewById(R.id.profile_picture);
        profileBadge = view.findViewById(R.id.profile_badge);

        logoutBtn = view.findViewById(R.id.logout_btn);
        deleteAccountBtn = view.findViewById(R.id.delete_account_btn);

        nameTxt.setText(User.CURRENT_USER.getName());
        pointTxt.setText(String.format("%,d Points", User.CURRENT_USER.getPoint()));

        ImageViewCompat.setImageTintList(profileBadge, ColorStateList.valueOf(ContextCompat.getColor(view.getContext(), User.CURRENT_USER.getBadgeColor())));

//        Log.d("coba", "onCreateView: " + User.CURRENT_USER.getProfilePicture());
        Database.showImage(User.CURRENT_USER.getProfilePicture(), ((Activity)view.getContext()), profilePic);

        logoutBtn.setOnClickListener(v -> {
            UserController.logout(view.getContext());
        });

        deleteAccountBtn.setOnClickListener(v -> {
            UserController.deleteAccount(User.CURRENT_USER.getId());
            UserController.logout(view.getContext());
        });


        RecyclerView recyclerDonation, recyclerAdoption;
        AdoptionAdapter adoptionAdapter;
        DonationAdapter donationAdapter;

        ArrayList<Adoption> adoptions;
        ArrayList<Donation> donations;

        recyclerDonation = view.findViewById(R.id.profile_donation_history);
        recyclerDonation.setHasFixedSize(true);
        recyclerDonation.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerAdoption = view.findViewById(R.id.profile_adoption_history);
        recyclerAdoption.setHasFixedSize(true);
        recyclerAdoption.setLayoutManager(new LinearLayoutManager(view.getContext()));

        donations = new ArrayList<>();
        donationAdapter = new DonationAdapter(view.getContext(), donations, true);
        recyclerDonation.setAdapter(donationAdapter);

        adoptions = new ArrayList<>();
        adoptionAdapter = new AdoptionAdapter(view.getContext(), adoptions, true);
        recyclerAdoption.setAdapter(adoptionAdapter);

        AdoptionController.showAllAdoptionsByUser(adoptionAdapter, adoptions);
        DonationController.showAllDonationsByUser(donationAdapter, donations);

        return view;
    }
}