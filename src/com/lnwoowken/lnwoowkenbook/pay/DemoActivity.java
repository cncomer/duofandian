package com.lnwoowken.lnwoowkenbook.pay;

import com.lnwoowken.lnwoowkenbook.R;
import com.umpay.creditcard.android.UmpayActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author dy
 */
public class DemoActivity extends Activity {

    private static final int requestCode = 888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo);
        // TradeNo输入框
        final EditText tradNoEt = (EditText)findViewById(R.id.tradeNo_edt);
        // 确定支付按钮
        Button pay = (Button)findViewById(R.id.pay_btn);
        pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 调用SDK进行支付
                startSdkToPay(tradNoEt.getText().toString(), 9);

            }
        });
    }

    /**
     * 在这里接收并处理支付结果
     * 
     * @param requestCode 支付请求码
     * @param resultCode SDK固定返回88888
     * @param data 支付结果和结果描述信息
     * @author niexuyang 2012-8-20
     */
    @SuppressWarnings("static-access")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (this.requestCode == requestCode && resultCode == 88888) {
            Toast.makeText(
                    this,
                    resultCode + " umpResultMessage:" + data.getStringExtra("umpResultMessage")
                            + "\n umpResultCode:" + data.getStringExtra("umpResultCode")+"\n orderId:"+data.getStringExtra("orderId"),
                    Toast.LENGTH_LONG).show();
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Toast.makeText(this, "支付失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 调用SDK进行支付
     * 
     * @param tradNo 下单获得的交易号
     * @param payType 当前需要的支付类型
     * @author niexuyang 2012-8-28
     */
    private void startSdkToPay(String tradNo, int payType) {
        // 跳转到SDK页面
        // 将输入的参数传入Activity
        Intent intent = new Intent();
        intent.putExtra("tradeNo", tradNo);
        intent.putExtra("payType", payType);
        intent.setClass(DemoActivity.this, UmpayActivity.class);
        startActivityForResult(intent, requestCode);
    }
}