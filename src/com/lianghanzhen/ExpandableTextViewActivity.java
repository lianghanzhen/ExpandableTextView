package com.lianghanzhen;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ExpandableTextViewActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final String disableText = "Line #1: ExpandableTextView has more than 3 lines\nLine #2: Can not expand\nLine #3\nLine #4\nLine #5\nLine #6";
        final String enableText = "Line #1: ExpandableTextView has more than 3 lines\nLine #2: Can expand\nLine #3\nLine #4\nLine #5\nLine #6";
        final ExpandableTextView disableView = (ExpandableTextView) findViewById(R.id.can_not_expand);
        disableView.setText(disableText);
        disableView.setCollapseLines(4);
        ((ToggleButton) findViewById(R.id.enable_expand)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                disableView.setText(checked ? enableText : disableText);
                disableView.setViewExpandable(checked);
            }
        });

        final ExpandableTextView changeLinesView = (ExpandableTextView) findViewById(R.id.lines_manual);
        ((ToggleButton) findViewById(R.id.change_lines)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                changeLinesView.setCollapseLines(checked ? 4 : 3);
            }
        });

        final ExpandableTextView toggleView = (ExpandableTextView) findViewById(R.id.toggle_text_view);
        toggleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // nothing happen
                showToast("Nothing happen");
            }
        });
        toggleView.setExtraOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("Extra OnClickListener");
            }
        });
        toggleView.setOnExpandListener(new ExpandableTextView.OnExpandListener() {
            @Override
            public void onExpand(ExpandableTextView view, boolean isExpanded) {
                showToast(isExpanded ? "Expand" : "Collapse");
            }
        });
        findViewById(R.id.expand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView.expand();
            }
        });
        findViewById(R.id.collapse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView.collapse();
            }
        });
        findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleView.toggle();
            }
        });

        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        ExpandableTextView child = new ExpandableTextView(this);
        child.setCollapseLines(1);
        child.setText("Line #1\nLine #2\nLine #3");
        child.setTextColor(Color.WHITE);
        child.setBackgroundColor(Color.GRAY);
        container.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
