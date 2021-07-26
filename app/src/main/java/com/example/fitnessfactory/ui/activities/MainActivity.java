package com.example.fitnessfactory.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.fitnessfactory.R;
import com.example.fitnessfactory.data.models.Quote;
import com.example.fitnessfactory.utils.GuiUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    public static final String QUOTE = "quote";
    public static final String AUTHOR = "author";
    @BindView(R.id.tvClientName)
    TextView tvClientName;
    @BindView(R.id.edtQuote)
    EditText edtQuote;
    @BindView(R.id.edtAuthor)
    EditText edtAuthor;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.tvText)
    TextView tvText;

    private DocumentReference docReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initComponents();
    }

    private void initComponents() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
           return;
        }
        tvClientName.setText(user.getDisplayName());
        btnSave.setOnClickListener(view -> dispatchData());
        docReference = FirebaseFirestore.getInstance().document("dataCollection/quotes");
    }

    private void fetchData(DocumentSnapshot docSnapshot) {
        try {
            Quote quote = docSnapshot.toObject(Quote.class);
            tvText.setText(quote.getQuote().concat(" - ").concat(quote.getAuthor()));
        } catch (Exception e) {
            GuiUtils.showMessage(e.getLocalizedMessage());
        }
    }

    private void dispatchData() {
        String quote = edtQuote.getText().toString();
        String author = edtAuthor.getText().toString();

        if (quote.isEmpty() || author.isEmpty()) {
            return;
        }
        Map<String, Object> data = new HashMap<>();
        data.put(QUOTE, quote);
        data.put(AUTHOR, author);
        docReference.set(data).addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                edtQuote.setText("");
                edtAuthor.setText("");
                GuiUtils.showMessage("Quote added");
            }
            else {
                GuiUtils.showMessage("Failed!");
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        docReference.addSnapshotListener(this, (docSnapshot, exception) -> {
            fetchData(docSnapshot);
        });
    }
}
