// Generated code from Butter Knife. Do not modify!
package com.example.puneetkumar.myapplication.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class QuestionActivity$$ViewBinder<T extends com.example.puneetkumar.myapplication.activities.QuestionActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624064, "field 'mPager'");
    target.mPager = finder.castView(view, 2131624064, "field 'mPager'");
    view = finder.findRequiredView(source, 2131624063, "field 'tvQuestionAnswered'");
    target.tvQuestionAnswered = finder.castView(view, 2131624063, "field 'tvQuestionAnswered'");
  }

  @Override public void unbind(T target) {
    target.mPager = null;
    target.tvQuestionAnswered = null;
  }
}
