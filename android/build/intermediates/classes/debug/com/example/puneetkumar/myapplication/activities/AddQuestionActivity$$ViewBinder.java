// Generated code from Butter Knife. Do not modify!
package com.example.puneetkumar.myapplication.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddQuestionActivity$$ViewBinder<T extends com.example.puneetkumar.myapplication.activities.AddQuestionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624055, "field 'etQuestion'");
    target.etQuestion = finder.castView(view, 2131624055, "field 'etQuestion'");
    view = finder.findRequiredView(source, 2131624056, "field 'etAnswer1'");
    target.etAnswer1 = finder.castView(view, 2131624056, "field 'etAnswer1'");
    view = finder.findRequiredView(source, 2131624057, "field 'etAnswer2'");
    target.etAnswer2 = finder.castView(view, 2131624057, "field 'etAnswer2'");
    view = finder.findRequiredView(source, 2131624058, "field 'etAnswer3'");
    target.etAnswer3 = finder.castView(view, 2131624058, "field 'etAnswer3'");
    view = finder.findRequiredView(source, 2131624059, "field 'etAnswer4'");
    target.etAnswer4 = finder.castView(view, 2131624059, "field 'etAnswer4'");
    view = finder.findRequiredView(source, 2131624060, "field 'etTags'");
    target.etTags = finder.castView(view, 2131624060, "field 'etTags'");
    view = finder.findRequiredView(source, 2131624061, "field 'butAddQuestion' and method 'sendQuestionToServer'");
    target.butAddQuestion = finder.castView(view, 2131624061, "field 'butAddQuestion'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendQuestionToServer();
        }
      });
  }

  @Override public void unbind(T target) {
    target.etQuestion = null;
    target.etAnswer1 = null;
    target.etAnswer2 = null;
    target.etAnswer3 = null;
    target.etAnswer4 = null;
    target.etTags = null;
    target.butAddQuestion = null;
  }
}
