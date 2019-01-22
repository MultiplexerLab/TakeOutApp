package multiplexer.lab.takeout.ItemActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import multiplexer.lab.takeout.Helper.EndPoints;
import multiplexer.lab.takeout.MainActivity;
import multiplexer.lab.takeout.R;


public class ScanQRActivity extends AppCompatActivity {

    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    AlertDialog dialog;
    RequestQueue queue;
    private ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        qrCode = findViewById(R.id.qrcode);
        String data =getData();
        try {
            bitmap = TextToImageEncode(data);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        qrCode.setImageBitmap(bitmap);
        queue = Volley.newRequestQueue(this);


    }

    private String getData() {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);

        String Phone = pref.getString("phone", "");

        return "+88"+Phone;

    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        return true;
    }

    private Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;

        try {
            bitMatrix = new MultiFormatWriter().encode(Value, BarcodeFormat.DATA_MATRIX.QR_CODE, QRcodeWidth, QRcodeWidth, null);

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public void btnMenu(View view) {
        Intent intent = new Intent(ScanQRActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    public void getPoints(View view) {
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String status = pref.getString("status", "");

        if (!status.isEmpty()) {

            dialog = new AlertDialog.Builder(ScanQRActivity.this).create();
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.custom_dialog_points, null);
            dialog.setView(customView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            Button btn = customView.findViewById(R.id.btn_bonus_points);
            final EditText editText = customView.findViewById(R.id.invoiceNo);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().isEmpty()) {
                        Toast.makeText(ScanQRActivity.this, "Please insert your invoice no!", Toast.LENGTH_SHORT).show();
                    } else {
                        sendInvoiceNo(editText.getText().toString());
                    }
                }
            });
            dialog.show();
        } else {
            Intent intent = new Intent(ScanQRActivity.this, AddReferralActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void sendInvoiceNo(final String invoiceNo) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, EndPoints.GET_USE_COUPON + invoiceNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("PointsResponse", response.toString());
                        if (!response.equals("")) {
                            Toast.makeText(ScanQRActivity.this, "Congrats! You have got " + invoiceNo + " points!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Log.e("networkResponse", response.toString());
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "application/json"));
                            Log.i("resString", res);
                            if (res.contains("account")) {
                                Toast.makeText(ScanQRActivity.this, "Your account is not activated!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (UnsupportedEncodingException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                String accessToken = pref.getString("accessToken", "");
                Log.i("accessToken", accessToken);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };
        queue.add(stringRequest);
    }
}
