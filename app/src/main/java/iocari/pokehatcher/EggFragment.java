package iocari.pokehatcher;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class EggFragment extends Fragment {
    private View v;

    private EggVM eggVM;
    private PlayerVM playerVM;
    private DexVM dexVM;

    private ConstraintLayout eggLayout, eggHatchLayout, eggHatchMoneyLayout, eggHatchXPLayout;
    private ImageButton imgEgg, eggHatchImg;
    private TextView txtEgg, pbEgg, txtEggHatchName, txtNewPokemon, txtEggHatchMoney, txtEggHatchXP;
    private ImageView imgCrit, imgHatchGlow;

    private AnimatorSet hatchPokemon = new AnimatorSet();

    private ArrayList<Boolean> hatched;
    float initialEggHatchRewardsX;
    float initialEggHatchRewardsY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public EggFragment() {
        this.hatched = new ArrayList<>(Arrays.asList(false, false, false, false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_egg, container, false);

        this.playerVM = new ViewModelProvider(requireActivity()).get(PlayerVM.class);
        this.eggVM = new ViewModelProvider(requireActivity()).get(EggVM.class);
        this.dexVM = new ViewModelProvider(requireActivity()).get(DexVM.class);

        initView();

        v.post(new Runnable() {
            @Override
            public void run() {
                initAnim();
            }
        });

        return v;
    }

    private void initAnim() {
        this.initialEggHatchRewardsX = this.eggHatchMoneyLayout.getX();
        this.initialEggHatchRewardsY = this.eggHatchMoneyLayout.getY();
        this.hatchPokemon = new AnimatorSet();
        AnimatorSet hatchGlow = new AnimatorSet();

        float hatchInitialX = this.imgEgg.getX();

        ObjectAnimator eggHatch1 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX-10);
        eggHatch1.setDuration(100);
        ObjectAnimator eggHatch2 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX+10);
        eggHatch2.setDuration(100);
        ObjectAnimator eggHatch3 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX-10);
        eggHatch3.setDuration(100);
        ObjectAnimator eggHatch4 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX);
        eggHatch4.setDuration(100);
        ObjectAnimator eggHatch5 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX-20);
        eggHatch5.setDuration(100);
        eggHatch5.setStartDelay(300);
        ObjectAnimator eggHatch6 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX+20);
        eggHatch6.setDuration(100);
        ObjectAnimator eggHatch7 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX-20);
        eggHatch7.setDuration(100);
        ObjectAnimator eggHatch8 = ObjectAnimator.ofFloat(this.imgEgg, "X", hatchInitialX);
        eggHatch8.setDuration(100);

        ObjectAnimator eggHatch9 = ObjectAnimator.ofFloat(this.imgHatchGlow, "scaleX", 50);
        ObjectAnimator eggHatch10 = ObjectAnimator.ofFloat(this.imgHatchGlow, "scaleY", 50);
        ObjectAnimator eggHatch11 = ObjectAnimator.ofFloat(this.imgHatchGlow, "rotation", 720);
        hatchGlow.play(eggHatch9).with(eggHatch10);
        hatchGlow.play(eggHatch9).with(eggHatch11);
        hatchGlow.setDuration(500);
        hatchGlow.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                eggLayout.setVisibility(View.INVISIBLE);
                eggHatchLayout.setVisibility(View.VISIBLE);
                imgHatchGlow.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                imgHatchGlow.setVisibility(View.INVISIBLE);
            }
        });

        this.hatchPokemon.playSequentially(eggHatch1, eggHatch2, eggHatch3, eggHatch4, eggHatch5, eggHatch6, eggHatch7, eggHatch8, hatchGlow);
        this.hatchPokemon.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animator) {
                unsetTouchListeners();
                eggHatchAnimations(eggVM.getEggTypeValue());
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                setTouchListeners();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                setTouchListeners();
            }
        });
    }

    private void unsetTouchListeners() {
        imgEgg.setOnTouchListener(null);
        eggHatchImg.setOnTouchListener(null);
        try {
            ((MainActivity) requireActivity()).unsetPlayerListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTouchListeners() {
        imgEgg.setOnTouchListener(this::clickEgg);
        eggHatchImg.setOnTouchListener(this::clickEgg);
        try {
            ((MainActivity) requireActivity()).setPlayerListeners();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        eggLayout = v.findViewById(R.id.eggLayout);
        eggHatchLayout = v.findViewById(R.id.eggHatchLayout);
        eggHatchMoneyLayout = v.findViewById(R.id.eggHatchMoneyLayout);
        eggHatchXPLayout = v.findViewById(R.id.eggHatchXPLayout);

        imgEgg = v.findViewById(R.id.imgEgg);
        txtEgg = v.findViewById(R.id.txtEgg);
        pbEgg = v.findViewById(R.id.pbEgg);
        imgCrit = v.findViewById(R.id.imgCrit);

        txtEggHatchName = v.findViewById(R.id.txtEggHatchName);
        eggHatchImg = v.findViewById(R.id.imgEggHatch);
        txtNewPokemon = v.findViewById(R.id.txtEggHatchNew);
        imgHatchGlow = v.findViewById(R.id.imgHatchGlow);
        txtEggHatchMoney = v.findViewById(R.id.eggHatchMoneyTxt);
        txtEggHatchXP = v.findViewById(R.id.eggHatchXPTxt);

        updateEgg();

        setTouchListeners();
    }

    private boolean clickEgg(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.imgEgg.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100);
            if (imgCrit.getVisibility() == View.VISIBLE) imgCrit.setVisibility(View.INVISIBLE);

            if (this.hatched.get(this.eggVM.getEggTypeValue())) {
                resetEgg();
                return true;
            }

            int progress = (int) (128 * this.playerVM.getClickMultiplierValue());

            if (this.isCrit()) {
                progress *= 2;
                imgCrit.setVisibility(View.VISIBLE);
            }

            this.playerVM.setEggProgress(this.eggVM.getEggTypeValue(), this.playerVM.getEggProgress(this.eggVM.getEggTypeValue()) + progress);
            this.updateEgg();

            if (this.playerVM.getEggProgress(this.eggVM.getEggTypeValue()) >= this.eggVM.getEggStepsValue()) {
                this.hatchEgg();
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            this.imgEgg.animate().scaleX(1f).scaleY(1f).setDuration(100);
            return true;
        }
        return false;
    }

    private boolean isCrit() {
        Random random = new Random();
        return random.nextInt(100*100) < this.playerVM.getCritChanceValue()*100;
    }

    private void hatchEgg() {
        if (imgCrit.getVisibility() == View.VISIBLE) imgCrit.setVisibility(View.INVISIBLE);
        this.hatched.set(this.eggVM.getEggTypeValue(), true);

        Random random = new Random();
        ArrayList<Integer> eggPokemon = this.dexVM.getPokemonFromEggType(this.eggVM.getEggTypeValue());
        int egg = eggPokemon.get(random.nextInt(eggPokemon.size()));

        this.hatchedEgg(egg);
    }

    private void hatchedEgg(int i) {
        DexItem dexItem = this.dexVM.getDexItem(i);

        this.eggVM.setEggHatchName(dexItem.getName());
        this.eggVM.setEggHatchImgSrc(dexItem.getImgId());

        txtEggHatchName.setText(this.eggVM.getEggHatchNameValue());
        eggHatchImg.setImageResource(this.eggVM.getEggHatchImgSrcValue());
        if (!dexItem.hasCaught()) {
            txtNewPokemon.setVisibility(View.VISIBLE);
            this.hatchPokemon.start();
        } else {
            txtNewPokemon.setVisibility(View.INVISIBLE);
            eggLayout.setVisibility(View.INVISIBLE);
            eggHatchLayout.setVisibility(View.VISIBLE);
            this.eggHatchAnimations(this.eggVM.getEggTypeValue());
        }

        this.playerVM.hatchEgg(this.eggVM.getEggTypeValue());
        dexItem.hatch();
        this.dexVM.setDexItem(dexItem);
    }

    private void resetEgg() {
        if (this.eggVM.getEggTypeValue() != 0 && this.playerVM.getEggQty(this.eggVM.getEggTypeValue()) > 0) {
            this.changeEgg(this.eggVM.getEggTypeValue());
            return;
        }
        this.changeEgg(0);
    }

    public void changeEgg(int type) {
        if(this.hatched.get(this.eggVM.getEggTypeValue())) {
            this.hatched.set(this.eggVM.getEggTypeValue(), false);
            this.savePokemonInDexAnimation();
        }

        this.eggVM.setEggType(type);
        this.eggVM.setEggHatchMoneyTxt(this.playerVM.getEggMoney(type));

        switch (eggVM.getEggTypeValue()) {
            case 0:
                this.eggVM.setEggSteps(2560);
                this.eggVM.setImgSrc(R.drawable.egg_common);
                this.eggVM.setName("Common Egg");
                break;
            case 1:
                this.eggVM.setEggSteps(5120);
                this.eggVM.setImgSrc(R.drawable.egg_uncommon);
                this.eggVM.setName("Uncommon Egg");
                break;
            case 2:
                this.eggVM.setEggSteps(8960);
                this.eggVM.setImgSrc(R.drawable.egg_rare);
                this.eggVM.setName("Rare Egg");
                break;
            case 3:
                this.eggVM.setEggSteps(30720);
                this.eggVM.setImgSrc(R.drawable.egg_legendary);
                this.eggVM.setName("Legendary Egg");
                break;
            default:
                break;
        }

        this.updateEgg();
    }

    private void updateEgg() {
        imgEgg.setImageResource(this.eggVM.getImgSrcValue());
        txtEgg.setText(this.eggVM.getNameValue());
        pbEgg.setText(String.format("%d / %d", this.playerVM.getEggProgress(this.eggVM.getEggTypeValue()), this.eggVM.getEggStepsValue()));
        txtEggHatchMoney.setText(String.format("%d", this.eggVM.getEggHatchMoneyTxtValue()));
        txtEggHatchXP.setText(String.format("%d", this.eggVM.getEggHatchXPTxtValue()));
        this.eggLayout.setVisibility(View.VISIBLE);
    }

    private void savePokemonInDexAnimation() {
        float saveInitialY = this.eggHatchLayout.getTop();
        float saveFinalY = v.getBottom();

        this.eggHatchLayout.animate()
                .scaleX(0.05f)
                .setDuration(100)
                .withStartAction(() -> {
                    unsetTouchListeners();
                    this.eggHatchLayout.animate().y(saveFinalY).setDuration(300);
                })
                .withEndAction(() -> {
                    eggLayout.setVisibility(View.VISIBLE);
                    eggHatchLayout.setVisibility(View.INVISIBLE);
                    this.eggHatchLayout.animate().y(saveInitialY).scaleX(1f);
                    setTouchListeners();
                });
    }

    private void eggHatchAnimations(Integer eggType) {
        int[] location = new int[2];
        this.v.getLocationOnScreen(location);

        float finalMoneyX = this.v.getRight() - this.eggHatchMoneyLayout.getWidth();
        float finalXPX = 0f;
        float finalY = -location[1] + this.eggHatchMoneyLayout.getHeight()/2f;

        float deltaY = finalY - this.initialEggHatchRewardsY;

        this.eggHatchMoneyLayout.animate()
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = ((float) (valueAnimator.getAnimatedValue()));
                        float y = (float) Math.sin(value * -(3f/2f)*Math.PI );
                        if (value > 2f/3f) {
                            y *= deltaY;
                            eggHatchMoneyLayout.setAlpha(3*(1-value)); // Linear Interpolation x -> 2/3 to 1, y -> 1 to 0
                        } else {
                            y *= -50f;
                        }
                        eggHatchMoneyLayout.setY((float) (initialEggHatchRewardsY + y));
                    }
                })
                .x(finalMoneyX)
                .setDuration(1000)
                .withStartAction(() -> {
                    this.eggHatchMoneyLayout.setVisibility(View.VISIBLE);
                })
                .withEndAction(() -> {
                    this.eggHatchMoneyLayout.setVisibility(View.INVISIBLE);
                    this.playerVM.gainEggMoney(eggType);
                    this.eggHatchMoneyLayout.setX(initialEggHatchRewardsX);
                    this.eggHatchMoneyLayout.setY(initialEggHatchRewardsY);
                    this.eggHatchMoneyLayout.setAlpha(1f);
                });

        this.eggHatchXPLayout.animate()
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float value = ((float) (valueAnimator.getAnimatedValue()));
                        float y = (float) Math.sin(value * -(3f/2f)*Math.PI );
                        if (value > 2f/3f) {
                            y *= deltaY;
                            eggHatchXPLayout.setAlpha(3*(1-value)); // Linear Interpolation x -> 2/3 to 1, y -> 1 to 0
                        } else {
                            y *= -50f;
                        }
                        eggHatchXPLayout.setY((float) (initialEggHatchRewardsY + y));
                    }
                })
                .x(finalXPX)
                .setDuration(1000)
                .withStartAction(() -> {
                    this.eggHatchXPLayout.setVisibility(View.VISIBLE);
                })
                .withEndAction(() -> {
                    this.playerVM.gainEggXP(eggType);
                    this.eggHatchXPLayout.setVisibility(View.INVISIBLE);
                    this.txtEggHatchXP.setText(String.format("%d", this.eggVM.getEggHatchXPTxtValue()));
                    this.eggHatchXPLayout.setX(initialEggHatchRewardsX);
                    this.eggHatchXPLayout.setY(initialEggHatchRewardsY);
                    this.eggHatchXPLayout.setAlpha(1f);
                });
    }
}
