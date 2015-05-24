package com.firebase.firebaseofflinequeries;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "OfflineDinosaurs";

    private Query mCurrentQuery;
    private ChildEventListener mCurrentListener;
    private ValueEventListener mConnectedListener;
    private Firebase mScoresRef;
    private TextView mOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mOutput = (TextView) this.findViewById(R.id.output);

        mScoresRef = new Firebase("https://dinosaur-facts.firebaseio.com/scores");
        // Set up a notification to let us know when we're connected or disconnected from the Firebase servers
        mConnectedListener = mScoresRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    mOutput.append("\nConnected to Firebase");
                    Log.w(TAG, "Connected to Firebase");
                    Toast.makeText(MainActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    mOutput.append("\nDisconnected from Firebase");
                    Log.w(TAG, "Disconnected from Firebase");
                    Toast.makeText(MainActivity.this, "Disconnected from Firebase", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

        ((Spinner)this.findViewById(R.id.itemCount)).setSelection(1);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mScoresRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
    }

    public void onQueryButtonClick(View v) {
        Log.i(TAG, "onQueryButtonClick");

        if (mCurrentQuery != null && mCurrentListener != null) {
            Log.i(TAG, "Removing current child_ listener from query");
            mCurrentQuery.removeEventListener(mCurrentListener);
        }

        ChildEventListener loggingListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Log.i(TAG, "child_added: " + snapshot.getKey());
                mOutput.append("\nchild_added: " + snapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String s) {
                Log.i(TAG, "child_changed: "+snapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                Log.i(TAG, "child_removed: " + snapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChild) {
                Log.i(TAG, "child_moved: " + snapshot.getKey());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        ValueEventListener loggingValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.i(TAG, "value: Got a value with "+snapshot.getChildrenCount()+" children");
                mOutput.append("\nvalue: Got a value with " + snapshot.getChildrenCount() + " children");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        };
        int count = Integer.valueOf(((Spinner) this.findViewById(R.id.itemCount)).getSelectedItem().toString());
        mOutput.append("\nRunning query for " + count + " children, attaching on('child_' and a once('value' listeners");
        Log.i(TAG, "Running query for " + count + " children, attaching on('child_' and a once('value' listeners");
        mCurrentQuery = mScoresRef.orderByValue().limitToLast(count);
        mCurrentListener = mCurrentQuery.addChildEventListener(loggingListener);
        mScoresRef.orderByValue().limitToLast(count).addListenerForSingleValueEvent(loggingValueEventListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
