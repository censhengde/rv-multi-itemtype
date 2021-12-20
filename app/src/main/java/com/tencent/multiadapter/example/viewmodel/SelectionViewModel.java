package com.tencent.multiadapter.example.viewmodel;

import androidx.lifecycle.ViewModel;
import com.tencent.multiadapter.example.bean.BeanA;
import java.util.ArrayList;
import java.util.List;

/**
 * Author：岑胜德 on 2021/2/22 15:08
 *
 * 说明：
 */
public class SelectionViewModel extends ViewModel {

  public  final List<BeanA> items=new ArrayList<>();
  public SelectionViewModel(){
      for (int i=0;i<20;i++){
          items.add(new BeanA(0, "Item:"+i));
      }

  }

}
