package com.example.btlmobileapp;

import com.example.btlmobileapp.Object.CauHoi;
import com.example.btlmobileapp.adapter.BountyAdapter;
import com.example.btlmobileapp.Retrofit.ApiClient;
import com.example.btlmobileapp.Retrofit.ApiService;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayActivity extends AppCompatActivity {
    private MediaPlayer mediaPlay;
    private MediaPlayer mediaMain;
    private static final int PERMISSION_REQUEST_CODE = 1;
    ListView lsvBounty;
    BountyAdapter bountyAdapter;
    ArrayList<String> bountyList;
    CauHoi cauHoi;
    TextView txvCauHoi;
    Button btnAns1,btnAns2,btnAns3,btnAns4;
    int QuestionPos = 1;
    private int correctAnswerIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_play);
        mediaPlay = MediaPlayer.create(this, R.raw.play_theme);
        mediaPlay.setLooping(true);
        mediaPlay.start();
        init();
        anhXa();
        setUp();
        setClick();
        fetchQuestions();
        ImageView imgBack = (ImageView) findViewById(R.id.imgBackHome);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPause();
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageView imgChangeQues = findViewById(R.id.imgChangeQues);
        imgChangeQues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgChangeQues.setVisibility(View.GONE);
                fetchQuestions();
                ShowQuestion();
            }
        });

        ImageView img5050 = findViewById(R.id.img5050);
        img5050.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Button> buttons = new ArrayList<>();
                buttons.add(btnAns1);
                buttons.add(btnAns2);
                buttons.add(btnAns3);
                buttons.add(btnAns4);

                int hiddenCount = 0;
                for (int i = 0; i < buttons.size(); i++) {
                    if (i != correctAnswerIndex && hiddenCount < 2) {
                        buttons.get(i).setText(" "); // Ẩn nút chứa đáp án sai
                        hiddenCount++;
                    }
                }
            img5050.setVisibility(View.GONE);
            }
        });

        ImageView imgAskFriend = findViewById(R.id.imgAskFriend);
        imgAskFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Button> buttons = new ArrayList<>();
                buttons.add(btnAns1);
                buttons.add(btnAns2);
                buttons.add(btnAns3);
                buttons.add(btnAns4);

                for (int i = 0; i < buttons.size(); i++) {
                    if (i != correctAnswerIndex ) {
                        buttons.get(i).setText(" ");
                    }
                }
                imgAskFriend.setVisibility(View.GONE);
            }
        });

        ImageView imgGuestHelp = findViewById(R.id.imgGuestHelp);
        imgGuestHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogGuestHelp dialog = new DialogGuestHelp(
                        PlayActivity.this,
                        correctAnswerIndex
                );
                dialog.show();
                imgGuestHelp.setVisibility(View.GONE);

            }
        });
    }

    protected void onPause() {
        super.onPause();
        if (mediaPlay != null && mediaPlay.isPlaying()) {
            mediaPlay.stop();
            mediaPlay.release();
            mediaPlay = null;
        }
    }

    protected void onResume() {
        super.onResume();
        if (mediaPlay == null) {
            mediaPlay = MediaPlayer.create(this, R.raw.main_theme);
            mediaPlay.setLooping(true);
            mediaPlay.start();
        }
    }

    public void init(){
        bountyList = new ArrayList<>();
        bountyList.add("200.000 đồng");
        bountyList.add("400.000 đồng");
        bountyList.add("600.000 đồng");
        bountyList.add("1.000.000 đồng");
        bountyList.add("2.000.000 đồng");
        bountyList.add("3.000.000 đồng");
        bountyList.add("6.000.000 đồng");
        bountyList.add("10.000.000 đồng");
        bountyList.add("14.000.000 đồng");
        bountyList.add("22.000.000 đồng");
        bountyList.add("30.000.000 đồng");
        bountyList.add("40.000.000 đồng");
        bountyList.add("80.000.000 đồng");
        bountyList.add("150.000.000 đồng");
        bountyList.add("250.000.000 đồng");

        bountyAdapter = new BountyAdapter(this,0,bountyList);
        cauHoi = new CauHoi();
    }

    public void anhXa(){
        lsvBounty = findViewById(R.id.lsvBounty);
        txvCauHoi = findViewById(R.id.txvCauHoi);
        btnAns1 = findViewById(R.id.btnAns1);
        btnAns2 = findViewById(R.id.btnAns2);
        btnAns3 = findViewById(R.id.btnAns3);
        btnAns4 = findViewById(R.id.btnAns4);

    }

    public void setUp(){
        lsvBounty.setAdapter(bountyAdapter);
    }

    public void setClick() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAns((Button) view);
            }

        };
        btnAns1.setOnClickListener(listener);
        btnAns2.setOnClickListener(listener);
        btnAns3.setOnClickListener(listener);
        btnAns4.setOnClickListener(listener);
    }

    public void checkAns(Button answer) {
        mediaPlay.pause();
        String ans = answer.getText().toString();
        final int clickColor = getResources().getColor(R.color.click_color);
        final int correctColor = getResources().getColor(R.color.correct_color);
        final int blinkColor = getResources().getColor(R.color.blink_color);

        final GradientDrawable originalDrawable = (GradientDrawable) answer.getBackground().mutate();
        originalDrawable.setColor(clickColor);
        new Handler().postDelayed(() -> {
            if (ans.equals(cauHoi.getDapAnDung())) {
                mediaMain = MediaPlayer.create(this, R.raw.correct_ans);
                mediaMain.start();
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), correctColor, blinkColor);
                colorAnimation.setDuration(500);
                colorAnimation.setRepeatMode(ValueAnimator.REVERSE);
                colorAnimation.setRepeatCount(ValueAnimator.INFINITE);

                colorAnimation.addUpdateListener(animator -> {
                            originalDrawable.setColor((int) animator.getAnimatedValue());
                            answer.setBackground(originalDrawable);
                        });
                colorAnimation.start();

                new Handler().postDelayed(() -> {
                    colorAnimation.cancel(); // Dừng hiệu ứng nhấp nháy sau 2 giây
                    answer.setBackgroundColor(correctColor); // Đặt lại màu nền đúng
                    QuestionPos++;
                    if (QuestionPos > 15) {
                        QuestionPos = 15;
                        saveFinalResultsToFile();
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.dialog_complete, null);

                        TextView messageTextView = dialogView.findViewById(R.id.dialog_message);
                        Button okButton = dialogView.findViewById(R.id.btnConfirm);

                        AlertDialog dialog = new AlertDialog.Builder(PlayActivity.this)
                                .setView(dialogView)
                                .create();

                        okButton.setOnClickListener(v -> {
                            Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        });

                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.borded);
                        dialog.show();
                    } else {
                        resetAnswerButtons();
                        mediaPlay.start();
                        mediaPlay.setLooping(true);
                        fetchQuestions();
                        ShowQuestion();

                    }
                }, 2000);

            }
            else {
                answer.setBackgroundResource(R.drawable.wrong_ans_bg);
                new Handler().postDelayed(() -> {
                    saveFinalResultsToFile();
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);
                    Button okBtn = dialogView.findViewById(R.id.btnConfirm);
                    AlertDialog dialog = new AlertDialog.Builder(PlayActivity.this)
                            .setView(dialogView)
                            .create();

                    okBtn.setOnClickListener(v -> {
                        Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    });

                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.borded);
                    dialog.show();
                }, 1000);
            }
        }, 5000); // Tạm dừng ngắn sau khi nhấn nút để hiển thị màu click_ans
    }

    private void resetAnswerButtons() {
        Button[] buttons = {btnAns1, btnAns2, btnAns3, btnAns4};
        for (Button btn : buttons) {
            btn.setBackgroundResource(R.drawable.borded); // Đặt lại màu nền mặc định
        }
    }

    public void fetchQuestions() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<CauHoi> call = apiService.getQuestion(QuestionPos);
        call.enqueue(new Callback<CauHoi>() {
            @Override
            public void onResponse(Call<CauHoi> call, Response<CauHoi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CauHoi question = response.body();
                    if (question != null) {
                        cauHoi.setNoiDung(question.getNoiDung());
                        cauHoi.setDapAnDung(question.getDapAnDung());
                        cauHoi.setDapAnSai1(question.getDapAnSai1());
                        cauHoi.setDapAnSai2(question.getDapAnSai2());
                        cauHoi.setDapAnSai3(question.getDapAnSai3());
                        ShowQuestion();
                    }
                } else {
                    Toast.makeText(PlayActivity.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            };

            @Override
            public void onFailure(Call<CauHoi> call, Throwable t) {
                Toast.makeText(PlayActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    };

    public void ShowQuestion() {
        txvCauHoi.setText(cauHoi.getNoiDung());
        ArrayList<String> arrAnswers = new ArrayList<>();
        arrAnswers.add(cauHoi.getDapAnSai1());
        arrAnswers.add(cauHoi.getDapAnSai2());
        arrAnswers.add(cauHoi.getDapAnSai3());
        arrAnswers.add(cauHoi.getDapAnDung());
        Collections.shuffle(arrAnswers);
        btnAns1.setText(arrAnswers.get(0));
        btnAns2.setText(arrAnswers.get(1));
        btnAns3.setText(arrAnswers.get(2));
        btnAns4.setText(arrAnswers.get(3));
        correctAnswerIndex = arrAnswers.indexOf(cauHoi.getDapAnDung());

        bountyAdapter.setViTri(QuestionPos);
    }

    private void saveFinalResultsToFile() {
        String content = Integer.toString(QuestionPos); // Chuyển giá trị số thành chuỗi
        File path = new File(getFilesDir(), "ResultLog"); // Thư mục lưu trữ
        File file = new File(path, "ketqua.txt"); // Tệp lưu trữ

        try {
            // Tạo thư mục nếu chưa tồn tại
            if (!path.exists()) {
                path.mkdirs();
            }

            // Tạo tệp nếu chưa tồn tại
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Log.d("FileCreation", "File created successfully");
                } else {
                    Log.d("FileCreation", "File already exists or could not be created");
                }
            }

            // Ghi dữ liệu vào tệp
            try (FileOutputStream fos = new FileOutputStream(file, true)) {
                fos.write((content + "\n").getBytes());
                Log.d("FileWrite", "Data written successfully");
                Log.d("FilePath", "File: " + file.getAbsolutePath());
            }

            // Đọc dữ liệu từ tệp để xác nhận
            String readContent = readFinalResultsFromFile();
            Log.d("FileRead", "File content: " + readContent);

        } catch (IOException e) {
            Log.e("FileWriteError", "Error writing to file", e); // Ghi lỗi nếu có
            Toast.makeText(this, "Lỗi khi lưu kết quả: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String readFinalResultsFromFile() {
        File path = new File(getFilesDir(), "ResultLog");
        File file = new File(path, "ketqua.txt");
        StringBuilder content = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(file)) {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            Log.e("FileReadError", "Error reading from file", e);
            Toast.makeText(this, "Lỗi khi đọc kết quả: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return content.toString();
    }


}


