// Generated by view binder compiler. Do not edit!
package com.example.cvt.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.cvt.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CollectionListRowBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView collectionTime;

  @NonNull
  public final TextView collectionTitle;

  @NonNull
  public final TextView collectionType;

  private CollectionListRowBinding(@NonNull RelativeLayout rootView,
      @NonNull TextView collectionTime, @NonNull TextView collectionTitle,
      @NonNull TextView collectionType) {
    this.rootView = rootView;
    this.collectionTime = collectionTime;
    this.collectionTitle = collectionTitle;
    this.collectionType = collectionType;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CollectionListRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CollectionListRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.collection_list_row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CollectionListRowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.collection_time;
      TextView collectionTime = ViewBindings.findChildViewById(rootView, id);
      if (collectionTime == null) {
        break missingId;
      }

      id = R.id.collection_title;
      TextView collectionTitle = ViewBindings.findChildViewById(rootView, id);
      if (collectionTitle == null) {
        break missingId;
      }

      id = R.id.collection_type;
      TextView collectionType = ViewBindings.findChildViewById(rootView, id);
      if (collectionType == null) {
        break missingId;
      }

      return new CollectionListRowBinding((RelativeLayout) rootView, collectionTime,
          collectionTitle, collectionType);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
