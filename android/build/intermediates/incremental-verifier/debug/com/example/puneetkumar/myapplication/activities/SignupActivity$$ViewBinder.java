// Generated code from Butter Knife. Do not modify!
package com.example.puneetkumar.myapplication.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SignupActivity$$ViewBinder<T extends com.example.puneetkumar.myapplication.activities.SignupActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624132, "field 'submitButton' and method 'submitData'");
    target.submitButton = finder.castView(view, 2131624132, "field 'submitButton'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.submitData();
        }
      });
    view = finder.findRequiredView(source, 2131624126, "field 'genderGroup'");
    target.genderGroup = finder.castView(view, 2131624126, "field 'genderGroup'");
    view = finder.findRequiredView(source, 2131624124, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131624124, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131624125, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131624125, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131624131, "field 'spinnerEthnicity'");
    target.spinnerEthnicity = finder.castView(view, 2131624131, "field 'spinnerEthnicity'");
  }

  @Override public void unbind(T target) {
    target.submitButton = null;
    target.genderGroup = null;
    target.etUsername = null;
    target.etPassword = null;
    target.spinnerEthnicity = null;
  }
}
