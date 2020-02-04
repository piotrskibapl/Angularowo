package pl.piotrskiba.angularowo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.adapters.ViewPagerFragmentAdapter;

public class ReportsHistoryFragmentContainerFragment extends Fragment {

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.viewpager)
    ViewPager2 mViewPager;

    public ReportsHistoryFragmentContainerFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports_history_fragment_container, container, false);

        ButterKnife.bind(this, view);

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_report_list);

        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), getLifecycle());
        adapter.addFragment(new ReportsHistoryFragment(false));
        adapter.addFragment(new ReportsHistoryFragment(true));

        mViewPager.setUserInputEnabled(false);
        mViewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mViewPager.setAdapter(adapter);

        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            if(position == 0)
                tab.setText(R.string.reports_own);
            else
                tab.setText(R.string.reports_others);
        }).attach();

        return view;
    }
}
