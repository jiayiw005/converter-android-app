// Generated by view binder compiler. Do not edit!
package com.example.cvt.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cvt.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final Button btnLogout;

  @NonNull
  public final EditText communityJoined;

  @NonNull
  public final ImageButton profilePic;

  @NonNull
  public final EditText signature;

  @NonNull
  public final EditText username;

  private FragmentProfileBinding(@NonNull FrameLayout rootView, @NonNull Button btnLogout,
      @NonNull EditText communityJoined, @NonNull ImageButton profilePic,
      @NonNull EditText signature, @NonNull EditText username) {
    this.rootView = rootView;
    this.btnLogout = btnLogout;
    this.communityJoined = communityJoined;
    this.profilePic = profilePic;
    this.signature = signature;
    this.username = username;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_logout;
      Button btnLogout = ViewBindings.findChildViewById(rootView, id);
      if (btnLogout == null) {
        break missingId;
      }

      id = R.id.community_joined;
      EditText communityJoined = ViewBindings.findChildViewById(rootView, id);
      if (communityJoined == null) {
        break missingId;
      }

      id = R.id.profilePic;
      ImageButton profilePic = ViewBindings.findChildViewById(rootView, id);
      if (profilePic == null) {
        break missingId;
      }

      id = R.id.signature;
      EditText signature = ViewBindings.findChildViewById(rootView, id);
      if (signature == null) {
        break missingId;
      }

      id = R.id.username;
      EditText username = ViewBindings.findChildViewById(rootView, id);
      if (username == null) {
        break missingId;
      }

      return new FragmentProfileBinding((FrameLayout) rootView, btnLogout, communityJoined,
          profilePic, signature, username);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
