/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.result;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import com.cncom.app.base.contact.AddrBookUtils;
import com.lnwoowken.lnwoowkenbook.R;

/**
 * Handles address book entries.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author yeluosuifeng2005@gmail.com (�¿�)
 */
public final class AddressBookResultHandler extends ResultHandler {

  private static final DateFormat[] DATE_FORMATS = {
    new SimpleDateFormat("yyyyMMdd"),
    new SimpleDateFormat("yyyyMMdd'T'HHmmss"),
    new SimpleDateFormat("yyyy-MM-dd"),
    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
  };

  private final boolean[] fields;
  private int buttonCount;
  private byte[] mPhoto = null;
  private AddressBookParsedResult addressResult;

  // This takes all the work out of figuring out which buttons/actions should be in which
  // positions, based on which fields are present in this barcode.
  private int mapIndexToAction(int index) {
    if (index < buttonCount) {
      int count = -1;
      for (int x = 0; x < MAX_BUTTON_COUNT; x++) {
        if (fields[x]) {
          count++;
        }
        if (count == index) {
          return x;
        }
      }
    }
    return -1;
  }

  public AddressBookResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
    addressResult = (AddressBookParsedResult) result;
    String[] phoneNumbers = addressResult.getPhoneNumbers();
    boolean hasPhoneNumber = phoneNumbers != null && phoneNumbers.length > 0;
    
    boolean hasBidMessage = false;
//    String Bid = addressResult.getBid();
//    boolean hasBidMessage = Bid != null 
//    	&& Bid.length() >= Contents.MingDang.MM_LENGTH;
    
    String cloudUrl = addressResult.getCloudUrl();
    if (!TextUtils.isEmpty(cloudUrl)) {
    	 hasBidMessage = true;
    }

    fields = new boolean[MAX_BUTTON_COUNT];
    fields[0] = true; // ����ɨ��
    //Add contact is always available
    fields[1] = true;
    fields[2] = false;  //���ز�����
    fields[3] = hasBidMessage;

    buttonCount = 0;
    for (int x = 0; x < MAX_BUTTON_COUNT; x++) {
      if (fields[x]) {
        buttonCount++;
      }
    }
  }

  @Override
  public int getButtonCount() {
    return buttonCount;
  }

  @Override
  public int getButtonText(int index) {
    int action = mapIndexToAction(index);
    switch (action) {
      case 0:
        return R.string.button_ignore;
      case 1:
        return R.string.button_add_contact;
      case 2:
        return R.string.button_dial;//����
      case 3:
        return R.string.button_get_cloud_card;
      default:
        throw new ArrayIndexOutOfBoundsException();
    }
  }

  @Override
  public void handleButtonPress(int index) {
//    AddressBookParsedResult addressResult = (AddressBookParsedResult) getResult();
//    String[] addresses = addressResult.getAddresses();
//    String address1 = addresses == null || addresses.length < 1 ? null : addresses[0];
    int action = mapIndexToAction(index);
    switch (action) {
      case 0:
    	gobackAndScan();
        break;
      case 1:
    	  Uri uri = AddrBookUtils.getInstance().createContactEntry(addressResult);
    	  AddrBookUtils.getInstance().viewContact(uri);
//    	  createContactEntry(addressResult, media);
//    	addContact(addressResult.getNames(), addressResult.getPhoneNumbers());
        break;
      case 2:
    	//if has more than one phone number, we call the first one.
        dialPhone(addressResult.getPhoneNumbers()[0]);//����
        break;
      case 3:
    	//��������Ƭ�������ϵ��
    	AddrBookUtils.getInstance().downloadAndViewContactLock(addressResult.getBid(), true);
        //sendEmail(addressResult.getEmails()[0], null, null);
    	break;
      default:
        break;
    }
  }

private static Date parseDate(String s) {
    for (DateFormat currentFomat : DATE_FORMATS) {
      synchronized (currentFomat) {
        currentFomat.setLenient(false);
        Date result = currentFomat.parse(s, new ParsePosition(0));
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  // Overriden so we can hyphenate phone numbers, format birthdays, and bold the name.
  @Override
  public CharSequence getDisplayContents() {
    AddressBookParsedResult result = (AddressBookParsedResult) getResult();
    StringBuilder contents = new StringBuilder();
    ParsedResult.maybeAppend(result.getNames(), contents);
    int namesLength = contents.length();

    String pronunciation = result.getPronunciation();
    if (pronunciation != null && pronunciation.length() > 0) {
      contents.append("\n(");
      contents.append(pronunciation);
      contents.append(')');
    }

    ParsedResult.maybeAppend(result.getTitle(), contents);
    ParsedResult.maybeAppend(result.getOrg(), contents);
    ParsedResult.maybeAppend(result.getAddresses(), contents);
    String[] numbers = result.getPhoneNumbers();
    if (numbers != null) {
      for (String number : numbers) {
        ParsedResult.maybeAppend(PhoneNumberUtils.formatNumber(number), contents);
      }
    }
    ParsedResult.maybeAppend(result.getEmails(), contents);
    ParsedResult.maybeAppend(result.getURL(), contents);

    String birthday = result.getBirthday();
    if (birthday != null && birthday.length() > 0) {
      Date date = parseDate(birthday);
      if (date != null) {
        ParsedResult.maybeAppend(DateFormat.getDateInstance().format(date.getTime()), contents);
      }
    }
    ParsedResult.maybeAppend(result.getNote(), contents);

    if (namesLength > 0) {
      // Bold the full name to make it stand out a bit.
      Spannable styled = new SpannableString(contents.toString());
      styled.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, namesLength, 0);
      return styled;
    } else {
      return contents.toString();
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_address_book;
  }
}
