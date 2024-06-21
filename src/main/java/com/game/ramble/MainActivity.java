package com.game.ramble;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView iv_card1, iv_card2, iv_card3, iv_card4, iv_card5, iv_card6, iv_card7, iv_card8, iv_card9, iv_card10, iv_card11, iv_card12;
    Button btn_home, btn_reset, btn_next;
    List<String> itemList = new ArrayList<>();
    Random rand = new Random();
    int num;
    static String cardFlipped1 = "", cardFlipped2 = "";
    int numFlippedCards = 0;
    TextView tv_won;
    MediaPlayer main_bgmusic, click_fxmusic, reset_fxmusic, won_fxmusic, invalid_fxmusic;
    boolean isFirst;
    private final Handler timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        isFirst = intent.getBooleanExtra("isFirst", false);

        if (isFirst) {
            main_bgmusic = MediaPlayer.create(this, R.raw.main_bgmusic);
            main_bgmusic.setLooping(true);
            main_bgmusic.start();
        }

        click_fxmusic = MediaPlayer.create(this, R.raw.click_fxmusic);
        reset_fxmusic = MediaPlayer.create(this, R.raw.reset_fxmusic);
        won_fxmusic = MediaPlayer.create(this, R.raw.won_fxmusic);
        invalid_fxmusic = MediaPlayer.create(this, R.raw.invalid_fxmusic);

        itemList.add("Pikachu1");
        itemList.add("Pikachu2");
        itemList.add("Mario1");
        itemList.add("Mario2");
        itemList.add("Sonic1");
        itemList.add("Sonic2");
        itemList.add("Steve1");
        itemList.add("Steve2");
        itemList.add("Kirby1");
        itemList.add("Kirby2");
        itemList.add("Pacman1");
        itemList.add("Pacman2");

        iv_card1 = findViewById(R.id.iv_card1);
        iv_card2 = findViewById(R.id.iv_card2);
        iv_card3 = findViewById(R.id.iv_card3);
        iv_card4 = findViewById(R.id.iv_card4);
        iv_card5 = findViewById(R.id.iv_card5);
        iv_card6 = findViewById(R.id.iv_card6);
        iv_card7 = findViewById(R.id.iv_card7);
        iv_card8 = findViewById(R.id.iv_card8);
        iv_card9 = findViewById(R.id.iv_card9);
        iv_card10 = findViewById(R.id.iv_card10);
        iv_card11 = findViewById(R.id.iv_card11);
        iv_card12 = findViewById(R.id.iv_card12);

        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(view -> {
            reset_fxmusic.start();
            main_bgmusic.stop();
            new Handler().postDelayed(this::finish, 1000);
        });

        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(view -> {
            resetCards();
        });

        tv_won = findViewById(R.id.tv_won);
        tv_won.setVisibility(View.INVISIBLE);

        btn_next = findViewById(R.id.btn_next);
        btn_next.setVisibility(View.INVISIBLE);
        btn_next.setOnClickListener(view -> {
            click_fxmusic.start();
            Intent newGame = new Intent(MainActivity.this, MainActivity.class);
            startActivity(newGame);
            finish();
        });

        setCards();

        timerHandler.postDelayed(timerRunnable, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isFirst) {
            if (main_bgmusic != null) {
                main_bgmusic.stop();
                main_bgmusic.release();
            }
        }

        if (click_fxmusic != null) {
            click_fxmusic.stop();
            click_fxmusic.release();
        }

        if (reset_fxmusic != null) {
            reset_fxmusic.stop();
            reset_fxmusic.release();
        }

        if (won_fxmusic != null) {
            won_fxmusic.stop();
            won_fxmusic.release();
        }

        if (invalid_fxmusic != null) {
            invalid_fxmusic.stop();
            invalid_fxmusic.release();
        }
    }

    private final Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if (allCardsShown()) {
                won_fxmusic.start();
                tv_won.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                timerHandler.removeCallbacks(this);
            } else {
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    public void setCards(){
        setCard1();
        setCard2();
        setCard3();
        setCard4();
        setCard5();
        setCard6();
        setCard7();
        setCard8();
        setCard9();
        setCard10();
        setCard11();
        setCard12();

        new Handler().postDelayed(this::cardClick, 5000);
    }

    public void cardClick(){
        iv_card1.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card1.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card1, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card1, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card1.getTag();
                    switch (tag){
                        case "Pikachu":
                            iv_card1.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card1.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card1.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card1.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":
                            iv_card1.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card1.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card1.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if(cardFlipped1.isEmpty()){
                cardFlipped1 = "card1";
            }
            else{
                cardFlipped2 = "card1";
            }
            isMatched();
        });

        iv_card2.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card2.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card2, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card2, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card2.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card2.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card2.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card2.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card2.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":
                            iv_card2.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card2.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card2.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card2";
            } else {
                cardFlipped2 = "card2";
            }
            isMatched();
        });

        iv_card3.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card3.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card3, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card3, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card3.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card3.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card3.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card3.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card3.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":
                            iv_card3.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card3.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card3.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card3";
            } else {
                cardFlipped2 = "card3";
            }
            isMatched();
        });

        iv_card4.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card4.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card4, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card4, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card4.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card4.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card4.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card4.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card4.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card4.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card4.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card4.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card4";
            } else {
                cardFlipped2 = "card4";
            }
            isMatched();
        });

        iv_card5.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card5.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card5, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card5, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card5.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card5.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card5.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card5.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card5.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card5.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card5.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card5.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card5";
            } else {
                cardFlipped2 = "card5";
            }
            isMatched();
        });

        iv_card6.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card6.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card6, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card6, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card6.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card6.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card6.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card6.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card6.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card6.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card6.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card6.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card6";
            } else {
                cardFlipped2 = "card6";
            }
            isMatched();
        });

        iv_card7.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card7.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card7, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card7, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card7.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card7.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card7.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card7.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card7.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card7.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card7.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card7.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card7";
            } else {
                cardFlipped2 = "card7";
            }
            isMatched();
        });

        iv_card8.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card8.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card8, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card8, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card8.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card8.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card8.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card8.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card8.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card8.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card8.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card8.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card8";
            } else {
                cardFlipped2 = "card8";
            }
            isMatched();
        });

        iv_card9.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card9.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card9, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card9, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card9.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card9.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card9.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card9.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card9.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card9.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card9.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card9.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card9";
            } else {
                cardFlipped2 = "card9";
            }
            isMatched();
        });

        iv_card10.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card10.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card10, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card10, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card10.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card10.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card10.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card10.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card10.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card10.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card10.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card10.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card10";
            } else {
                cardFlipped2 = "card10";
            }
            isMatched();
        });

        iv_card11.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card11.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card11, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card11, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card11.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card11.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card11.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card11.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card11.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card11.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card11.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card11.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card11";
            } else {
                cardFlipped2 = "card11";
            }
            isMatched();
        });

        iv_card12.setOnClickListener(view -> {
            if (numFlippedCards >= 2 || iv_card12.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState()) {
                invalid_fxmusic.start();
                return;
            }
            click_fxmusic.start();
            ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card12, "scaleX", 1f, 0f);
            anime1.setInterpolator(new DecelerateInterpolator());

            ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card12, "scaleX", 0f, 1f);
            anime2.setInterpolator(new AccelerateInterpolator());

            anime1.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    String tag = (String) iv_card12.getTag();
                    switch (tag) {
                        case "Pikachu":
                            iv_card12.setImageResource(R.drawable.pikachu);
                            break;

                        case "Mario":
                            iv_card12.setImageResource(R.drawable.mario);
                            break;

                        case "Sonic":
                            iv_card12.setImageResource(R.drawable.sonic);
                            break;

                        case "Steve":
                            iv_card12.setImageResource(R.drawable.steve);
                            break;

                        case "Kirby":

                            iv_card12.setImageResource(R.drawable.kirby);
                            break;

                        case "Pacman":
                            iv_card12.setImageResource(R.drawable.pacman);
                            break;
                    }
                    iv_card12.setBackgroundResource(R.drawable.template);
                    anime2.start();
                }
            });
            anime1.start();
            if (cardFlipped1.isEmpty()) {
                cardFlipped1 = "card12";
            } else {
                cardFlipped2 = "card12";
            }
            isMatched();
        });
    }

    public void isMatched() {
        if (!cardFlipped1.isEmpty() && !cardFlipped2.isEmpty()) {
            String tag1 = "", tag2 = "";

            switch (cardFlipped1){
                case "card1":
                    tag1 = (String) iv_card1.getTag();
                    break;

                case "card2":
                    tag1 = (String) iv_card2.getTag();
                    break;

                case "card3":
                    tag1 = (String) iv_card3.getTag();
                    break;

                case "card4":
                    tag1 = (String) iv_card4.getTag();
                    break;

                case "card5":
                    tag1 = (String) iv_card5.getTag();
                    break;

                case "card6":
                    tag1 = (String) iv_card6.getTag();
                    break;

                case "card7":
                    tag1 = (String) iv_card7.getTag();
                    break;

                case "card8":
                    tag1 = (String) iv_card8.getTag();
                    break;

                case "card9":
                    tag1 = (String) iv_card9.getTag();
                    break;

                case "card10":
                    tag1 = (String) iv_card10.getTag();
                    break;

                case "card11":
                    tag1 = (String) iv_card11.getTag();
                    break;

                case "card12":
                    tag1 = (String) iv_card12.getTag();
                    break;
            }

            switch (cardFlipped2){
                case "card1":
                    tag2 = (String) iv_card1.getTag();
                    break;

                case "card2":
                    tag2 = (String) iv_card2.getTag();
                    break;

                case "card3":
                    tag2 = (String) iv_card3.getTag();
                    break;

                case "card4":
                    tag2 = (String) iv_card4.getTag();
                    break;

                case "card5":
                    tag2 = (String) iv_card5.getTag();
                    break;

                case "card6":
                    tag2 = (String) iv_card6.getTag();
                    break;

                case "card7":
                    tag2 = (String) iv_card7.getTag();
                    break;

                case "card8":
                    tag2 = (String) iv_card8.getTag();
                    break;

                case "card9":
                    tag2 = (String) iv_card9.getTag();
                    break;

                case "card10":
                    tag2 = (String) iv_card10.getTag();
                    break;

                case "card11":
                    tag2 = (String) iv_card11.getTag();
                    break;

                case "card12":
                    tag2 = (String) iv_card12.getTag();
                    break;
            }

            if (!tag1.equals(tag2)) {
                flipCardBackOver(cardFlipped1);
                flipCardBackOver(cardFlipped2);
            }

            cardFlipped1 = "";
            cardFlipped2 = "";
        }
    }

    private void flipCardBackOver(String cardId) {
        switch (cardId) {
            case "card1":
                flipCard(iv_card1);
                break;

            case "card2":
                flipCard(iv_card2);
                break;

            case "card3":
                flipCard(iv_card3);
                break;

            case "card4":
                flipCard(iv_card4);
                break;

            case "card5":
                flipCard(iv_card5);
                break;

            case "card6":
                flipCard(iv_card6);
                break;

            case "card7":
                flipCard(iv_card7);
                break;

            case "card8":
                flipCard(iv_card8);
                break;

            case "card9":
                flipCard(iv_card9);
                break;

            case "card10":
                flipCard(iv_card10);
                break;

            case "card11":
                flipCard(iv_card11);
                break;

            case "card12":
                flipCard(iv_card12);
                break;
        }
    }

    private void flipCard(ImageView card) {
        numFlippedCards++;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(card, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        card.setImageResource(R.drawable.card);
                        anime2.start();
                        numFlippedCards--;
                    }
                });
                anime1.start();
            }
        }, 2000);
    }

    private void resetCards() {
        if (allCardsHidden()) {
            invalid_fxmusic.start();
            return;
        }
        reset_fxmusic.start();
        resetCard(iv_card1);
        resetCard(iv_card2);
        resetCard(iv_card3);
        resetCard(iv_card4);
        resetCard(iv_card5);
        resetCard(iv_card6);
        resetCard(iv_card7);
        resetCard(iv_card8);
        resetCard(iv_card9);
        resetCard(iv_card10);
        resetCard(iv_card11);
        resetCard(iv_card12);
    }

    private boolean allCardsHidden() {
        return iv_card1.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card2.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card3.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card4.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card5.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card6.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card7.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card8.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card9.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card10.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card11.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card12.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.card).getConstantState();
    }

    private boolean allCardsShown() {
        return iv_card1.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card2.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card3.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card4.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card5.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card6.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card7.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card8.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card9.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card10.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card11.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState() &&
                iv_card12.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.card).getConstantState();
    }

    private void resetCard(ImageView card) {
        ObjectAnimator anime1 = ObjectAnimator.ofFloat(card, "scaleX", 1f, 0f);
        anime1.setInterpolator(new DecelerateInterpolator());

        ObjectAnimator anime2 = ObjectAnimator.ofFloat(card, "scaleX", 0f, 1f);
        anime2.setInterpolator(new AccelerateInterpolator());

        anime1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                card.setImageResource(R.drawable.card);
                card.setBackgroundResource(0);
                anime2.start();
            }
        });
        anime1.start();
    }

    public void setCard1(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card1, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card1, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card1.setImageResource(R.drawable.pikachu);
                                iv_card1.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card1.setImageResource(R.drawable.mario);
                                iv_card1.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card1.setImageResource(R.drawable.sonic);
                                iv_card1.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card1.setImageResource(R.drawable.steve);
                                iv_card1.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card1.setImageResource(R.drawable.kirby);
                                iv_card1.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card1.setImageResource(R.drawable.pacman);
                                iv_card1.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card1.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card1, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card1, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card1.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard2(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card2, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card2, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card2.setImageResource(R.drawable.pikachu);
                                iv_card2.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card2.setImageResource(R.drawable.mario);
                                iv_card2.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card2.setImageResource(R.drawable.sonic);
                                iv_card2.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card2.setImageResource(R.drawable.steve);
                                iv_card2.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card2.setImageResource(R.drawable.kirby);
                                iv_card2.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card2.setImageResource(R.drawable.pacman);
                                iv_card2.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card2.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card2, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card2, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card2.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard3(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card3, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card3, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card3.setImageResource(R.drawable.pikachu);
                                iv_card3.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card3.setImageResource(R.drawable.mario);
                                iv_card3.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card3.setImageResource(R.drawable.sonic);
                                iv_card3.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card3.setImageResource(R.drawable.steve);
                                iv_card3.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card3.setImageResource(R.drawable.kirby);
                                iv_card3.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card3.setImageResource(R.drawable.pacman);
                                iv_card3.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card3.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card3, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card3, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card3.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard4(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card4, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card4, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card4.setImageResource(R.drawable.pikachu);
                                iv_card4.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card4.setImageResource(R.drawable.mario);
                                iv_card4.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card4.setImageResource(R.drawable.sonic);
                                iv_card4.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card4.setImageResource(R.drawable.steve);
                                iv_card4.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card4.setImageResource(R.drawable.kirby);
                                iv_card4.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card4.setImageResource(R.drawable.pacman);
                                iv_card4.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card4.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card4, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card4, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card4.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard5(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card5, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card5, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card5.setImageResource(R.drawable.pikachu);
                                iv_card5.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card5.setImageResource(R.drawable.mario);
                                iv_card5.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card5.setImageResource(R.drawable.sonic);
                                iv_card5.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card5.setImageResource(R.drawable.steve);
                                iv_card5.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card5.setImageResource(R.drawable.kirby);
                                iv_card5.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card5.setImageResource(R.drawable.pacman);
                                iv_card5.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card5.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card5, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card5, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card5.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard6(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card6, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card6, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card6.setImageResource(R.drawable.pikachu);
                                iv_card6.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card6.setImageResource(R.drawable.mario);
                                iv_card6.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card6.setImageResource(R.drawable.sonic);
                                iv_card6.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card6.setImageResource(R.drawable.steve);
                                iv_card6.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card6.setImageResource(R.drawable.kirby);
                                iv_card6.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card6.setImageResource(R.drawable.pacman);
                                iv_card6.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card6.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card6, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card6, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card6.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard7(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card7, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card7, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card7.setImageResource(R.drawable.pikachu);
                                iv_card7.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card7.setImageResource(R.drawable.mario);
                                iv_card7.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card7.setImageResource(R.drawable.sonic);
                                iv_card7.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card7.setImageResource(R.drawable.steve);
                                iv_card7.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card7.setImageResource(R.drawable.kirby);
                                iv_card7.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card7.setImageResource(R.drawable.pacman);
                                iv_card7.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card7.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card7, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card7, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card7.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard8(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card8, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card8, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card8.setImageResource(R.drawable.pikachu);
                                iv_card8.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card8.setImageResource(R.drawable.mario);
                                iv_card8.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card8.setImageResource(R.drawable.sonic);
                                iv_card8.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card8.setImageResource(R.drawable.steve);
                                iv_card8.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card8.setImageResource(R.drawable.kirby);
                                iv_card8.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card8.setImageResource(R.drawable.pacman);
                                iv_card8.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card8.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card8, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card8, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card8.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard9(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card9, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card9, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card9.setImageResource(R.drawable.pikachu);
                                iv_card9.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card9.setImageResource(R.drawable.mario);
                                iv_card9.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card9.setImageResource(R.drawable.sonic);
                                iv_card9.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card9.setImageResource(R.drawable.steve);
                                iv_card9.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card9.setImageResource(R.drawable.kirby);
                                iv_card9.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card9.setImageResource(R.drawable.pacman);
                                iv_card9.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card9.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card9, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card9, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card9.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard10(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card10, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card10, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card10.setImageResource(R.drawable.pikachu);
                                iv_card10.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card10.setImageResource(R.drawable.mario);
                                iv_card10.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card10.setImageResource(R.drawable.sonic);
                                iv_card10.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card10.setImageResource(R.drawable.steve);
                                iv_card10.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card10.setImageResource(R.drawable.kirby);
                                iv_card10.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card10.setImageResource(R.drawable.pacman);
                                iv_card10.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card10.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card10, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card10, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card10.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard11(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card11, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card11, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card11.setImageResource(R.drawable.pikachu);
                                iv_card11.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card11.setImageResource(R.drawable.mario);
                                iv_card11.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card11.setImageResource(R.drawable.sonic);
                                iv_card11.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card11.setImageResource(R.drawable.steve);
                                iv_card11.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card11.setImageResource(R.drawable.kirby);
                                iv_card11.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card11.setImageResource(R.drawable.pacman);
                                iv_card11.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card11.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card11, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card11, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card11.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }

    public void setCard12(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anime1 = ObjectAnimator.ofFloat(iv_card12, "scaleX", 1f, 0f);
                anime1.setInterpolator(new DecelerateInterpolator());

                ObjectAnimator anime2 = ObjectAnimator.ofFloat(iv_card12, "scaleX", 0f, 1f);
                anime2.setInterpolator(new AccelerateInterpolator());

                anime1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        num = rand.nextInt(itemList.size());
                        String selectedItem = itemList.get(num);
                        switch (selectedItem) {
                            case "Pikachu1":
                            case "Pikachu2":
                                iv_card12.setImageResource(R.drawable.pikachu);
                                iv_card12.setTag("Pikachu");
                                break;

                            case "Mario1":
                            case "Mario2":
                                iv_card12.setImageResource(R.drawable.mario);
                                iv_card12.setTag("Mario");
                                break;

                            case "Sonic1":
                            case "Sonic2":
                                iv_card12.setImageResource(R.drawable.sonic);
                                iv_card12.setTag("Sonic");
                                break;

                            case "Steve1":
                            case "Steve2":
                                iv_card12.setImageResource(R.drawable.steve);
                                iv_card12.setTag("Steve");
                                break;

                            case "Kirby1":
                            case "Kirby2":
                                iv_card12.setImageResource(R.drawable.kirby);
                                iv_card12.setTag("Kirby");
                                break;

                            case "Pacman1":
                            case "Pacman2":
                                iv_card12.setImageResource(R.drawable.pacman);
                                iv_card12.setTag("Pacman");
                                break;
                        }
                        itemList.remove(selectedItem);
                        iv_card12.setBackgroundResource(R.drawable.template);
                        anime2.start();
                    }
                });
                anime1.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator anime1Reverse = ObjectAnimator.ofFloat(iv_card12, "scaleX", 1f, 0f);
                        anime1Reverse.setInterpolator(new DecelerateInterpolator());

                        ObjectAnimator anime2Reverse = ObjectAnimator.ofFloat(iv_card12, "scaleX", 0f, 1f);
                        anime2Reverse.setInterpolator(new AccelerateInterpolator());

                        anime1Reverse.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                iv_card12.setImageResource(R.drawable.card);
                                anime2Reverse.start();
                            }
                        });
                        anime1Reverse.start();
                    }
                }, 2000);
            }
        }, 2000);
    }
}