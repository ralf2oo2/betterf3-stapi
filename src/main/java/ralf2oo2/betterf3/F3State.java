package ralf2oo2.betterf3;

import ralf2oo2.betterf3.modules.BaseModule;

import java.util.ArrayList;
import java.util.List;

public class F3State {
    public List<BaseModule> leftModules = new ArrayList<>();
    public List<BaseModule> rightModules = new ArrayList<>();

    public void clear(){
        leftModules.clear();
        rightModules.clear();
    }
}
