package com.nhom1.kttstoreapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nhom1.kttstoreapp.R;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment {

    private EditText etMinPrice, etMaxPrice;
    private Spinner spinnerSort;
    private RadioGroup rgStock;
    private CheckBox cbPromotion;
    private Button btnReset, btnApply;
    private OnFilterApplyListener listener;

    public interface OnFilterApplyListener {
        void onApply(Integer minPrice, Integer maxPrice, String sort, String stock, Boolean promotion);
    }

    public void setOnFilterApplyListener(OnFilterApplyListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_filter_bottom_sheet, container, false);

        etMinPrice = view.findViewById(R.id.etMinPrice);
        etMaxPrice = view.findViewById(R.id.etMaxPrice);
        spinnerSort = view.findViewById(R.id.spinnerSort);
        rgStock = view.findViewById(R.id.rgStock);
        cbPromotion = view.findViewById(R.id.cbPromotion);
        btnReset = view.findViewById(R.id.btnReset);
        btnApply = view.findViewById(R.id.btnApply);

        setupSpinner();

        btnReset.setOnClickListener(v -> resetFilters());
        btnApply.setOnClickListener(v -> applyFilters());

        return view;
    }

    private void setupSpinner() {
        String[] sortOptions = { "Mới nhất", "Giá tăng dần", "Giá giảm dần" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, sortOptions);
        spinnerSort.setAdapter(adapter);
    }

    private void resetFilters() {
        etMinPrice.setText("");
        etMaxPrice.setText("");
        spinnerSort.setSelection(0);
        rgStock.check(R.id.rbAllStock);
        cbPromotion.setChecked(false);
    }

    private void applyFilters() {
        if (listener != null) {
            String minPriceStr = etMinPrice.getText().toString();
            String maxPriceStr = etMaxPrice.getText().toString();
            Integer minPrice = minPriceStr.isEmpty() ? null : Integer.parseInt(minPriceStr);
            Integer maxPrice = maxPriceStr.isEmpty() ? null : Integer.parseInt(maxPriceStr);

            String sort = "newest"; // Default
            int sortPos = spinnerSort.getSelectedItemPosition();
            if (sortPos == 1)
                sort = "price_asc";
            else if (sortPos == 2)
                sort = "price_desc";

            String stock = "all";
            int checkedId = rgStock.getCheckedRadioButtonId();
            if (checkedId == R.id.rbInStock)
                stock = "in_stock";
            else if (checkedId == R.id.rbOutOfStock)
                stock = "out_of_stock";

            Boolean promotion = cbPromotion.isChecked() ? true : null;

            listener.onApply(minPrice, maxPrice, sort, stock, promotion);
            dismiss();
        }
    }
}
