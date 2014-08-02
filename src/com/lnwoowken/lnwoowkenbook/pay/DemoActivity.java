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
        // TradeNo�����
        final EditText tradNoEt = (EditText)findViewById(R.id.tradeNo_edt);
        // ȷ��֧����ť
        Button pay = (Button)findViewById(R.id.pay_btn);
        pay.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // ����SDK����֧��
                startSdkToPay(tradNoEt.getText().toString(), 9);

            }
        });
    }

    /**
     * ��������ղ�����֧�����
     * 
     * @param requestCode ֧��������
     * @param resultCode SDK�̶�����88888
     * @param data ֧������ͽ��������Ϣ
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
            Toast.makeText(this, "֧��ʧ��", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * ����SDK����֧��
     * 
     * @param tradNo �µ���õĽ��׺�
     * @param payType ��ǰ��Ҫ��֧������
     * @author niexuyang 2012-8-28
     */
    private void startSdkToPay(String tradNo, int payType) {
        // ��ת��SDKҳ��
        // ������Ĳ�������Activity
        Intent intent = new Intent();
        intent.putExtra("tradeNo", tradNo);
        intent.putExtra("payType", payType);
        intent.setClass(DemoActivity.this, UmpayActivity.class);
        startActivityForResult(intent, requestCode);
    }
}