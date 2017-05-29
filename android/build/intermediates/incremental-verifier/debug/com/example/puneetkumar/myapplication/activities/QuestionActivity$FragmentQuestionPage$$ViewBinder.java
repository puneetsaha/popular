// Generated code from Butter Knife. Do not modify!
package com.example.puneetkumar.myapplication.activities;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class QuestionActivity$FragmentQuestionPage$$ViewBinder<T extends com.example.puneetkumar.myapplication.activities.QuestionActivity.FragmentQuestionPage> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131624090, "field 'question'");
    target.question = finder.castView(view, 2131624090, "field 'question'");
    view = finder.findRequiredView(source, 2131624091, "field 'answer1' and method 'setAnswer1'");
    target.answer1 = finder.castView(view, 2131624091, "field 'answer1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAnswer1();
        }
      });
    view = finder.findRequiredView(source, 2131624093, "field 'answer2' and method 'setAnswer2'");
    target.answer2 = finder.castView(view, 2131624093, "field 'answer2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAnswer2();
        }
      });
    view = finder.findRequiredView(source, 2131624095, "field 'answer3' and method 'setAnswer3'");
    target.answer3 = finder.castView(view, 2131624095, "field 'answer3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAnswer3();
        }
      });
    view = finder.findRequiredView(source, 2131624097, "field 'answer4' and method 'setAnswer4'");
    target.answer4 = finder.castView(view, 2131624097, "field 'answer4'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAnswer4();
        }
      });
    view = finder.findRequiredView(source, 2131624092, "field 'progress1'");
    target.progress1 = finder.castView(view, 2131624092, "field 'progress1'");
    view = finder.findRequiredView(source, 2131624094, "field 'progress2'");
    target.progress2 = finder.castView(view, 2131624094, "field 'progress2'");
    view = finder.findRequiredView(source, 2131624096, "field 'progress3'");
    target.progress3 = finder.castView(view, 2131624096, "field 'progress3'");
    view = finder.findRequiredView(source, 2131624098, "field 'progress4'");
    target.progress4 = finder.castView(view, 2131624098, "field 'progress4'");
  }

  @Override public void unbind(T target) {
    target.question = null;
    target.answer1 = null;
    target.answer2 = null;
    target.answer3 = null;
    target.answer4 = null;
    target.progress1 = null;
    target.progress2 = null;
    target.progress3 = null;
    target.progress4 = null;
  }
}
