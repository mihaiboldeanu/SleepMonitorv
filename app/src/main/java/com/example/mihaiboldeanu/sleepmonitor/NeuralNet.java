package com.example.mihaiboldeanu.sleepmonitor;

/**
 * Created by Mihai Boldeanu on 31.01.2018.
 */
import android.util.Log;

import java.util.*;

public class NeuralNet {
    private static final String TAG = "NeuralNet";
    private double[][] layer_input;
    private double[] layer_hidden;

    private int[] input = new int[10];
    private double SDANN;
    private double RMSSD;
    private double SDSD;
    private double pNN50;
    private double pNN20;
    private int index = 0;
    private double output_NN = 0;

    public NeuralNet(double[][] layer_in, double[] layer_hid) {
        layer_input = layer_in;
        layer_hidden = layer_hid;

    }



    public void addNewData(int in)
    {   if (index <10)
            input[index] = in;
        else {
            index = 10;
            for (int i = 0; i<input.length;i++)
                input[i] = input[i+1];
            input[index] = in;
        calculateHRV();
    }
    }
    public void calculateHRV(){
        //SDANN
        float tmp = 0 ;
        for (int i =0 ; i<input.length ; i++ ) {
           tmp += input[i];
        }
        float avg_hrv = tmp/input.length;

        for (int i =0 ; i<input.length ; i++ ) {
            tmp += Math.pow(input[i]- avg_hrv,2);
        }
        SDANN = (float) (Math.sqrt(tmp/input.length) / 100.0);

        //RMSSD
        tmp = 0 ;
        for (int i =0 ; i<input.length-1 ; i++ ) {
            tmp += Math.pow(input[i] - input[i+1],2);
        }
        RMSSD = (float) (Math.sqrt(tmp/input.length) / 100.0);



        //SDSD
        tmp = 0 ;
        for (int i =0 ; i<input.length-1 ; i++ ) {
            tmp += Math.abs(input[i] - input[i+1])/SDANN;
        }
        SDSD = (float) (Math.sqrt(tmp/input.length) / 100.0);

        //pNN50
        tmp = 0;
        for (int i =0 ; i< input.length-1;i++){
            if (Math.abs(input[i] - input[i+1])>=50 ){
                tmp++;
            }
        }
        pNN50 = tmp/input.length;
        //pNN20
        tmp = 0;
        for (int i =0 ; i< input.length-1;i++){
            if (Math.abs(input[i] - input[i+1])>=20 ){
                tmp++;
            }
        }
        pNN20 = tmp/input.length;
        Log.d(TAG, "HRV");
        Log.d(TAG,  "SDANN");

    }
    public void neural_run(){
        double[] inputNN = new double[5];
        double[] input_NN_hidden = new double[3];
        inputNN[0] = SDANN;
        inputNN[1] = RMSSD;
        inputNN[2] = SDSD;
        inputNN[3] = pNN50;
        inputNN[4] = pNN20;
        double tmp = 0;

        for (int i = 0; i<3;i++){
            for (int j = 0 ; j<inputNN.length;j++){
                tmp += inputNN[j] * layer_input[j][i];
            }
            input_NN_hidden[i] = tmp;
            tmp = 0;

        }


        for (int j = 0 ; j<inputNN.length;j++){
            tmp += inputNN[j] * layer_hidden[j];
        }
        output_NN = tmp;
    }

    public boolean isSleeping(){
        if (output_NN<0.5)
            return false;
        else
            return true;
    }
}
