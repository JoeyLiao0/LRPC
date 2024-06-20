package Service;

import joey.common.annotation.LRPCService;

@LRPCService
public class Calculation {
    public int add(int a,int b){
        return a+b;
    }

    public int sub(int a,int b){
        return a-b;
    }

    public int mul(int a,int b){
        return a*b;
    }

    public int div(int a,int b){
        return a/b;
    }
}
