package com.aecb.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class EmirateChangeListenerValidation {
    public static void filterEID(Editable s, EditText editText, TextWatcher textWatcher) {

        /* we using custom id format so user entering value then we appending dash*/

        String val = s.toString();
        String a = "";
        String b = "";
        String c = "";
        String d = "";

        if (val != null && val.length() > 0) {
            val = val.replace("-", "");
            if (val.length() >= 3) {
                a = val.substring(0, 3);
            } else if (val.length() < 3) {
                a = val;
            }
            if (val.length() >= 7) {
                b = val.substring(3, 7);
                c = val.substring(7);
            } else if (val.length() > 3 && val.length() < 7) {
                b = val.substring(3);
            }

            if (val.length() >= 14) {
                c = val.substring(7, 14);
                d = val.substring(14);
            } else if (val.length() > 7 && val.length() < 14) {
                c = val.substring(7);
            }

            StringBuffer stringBuffer = new StringBuffer();
            if (a != null && a.length() > 0) {
                stringBuffer.append(a);
                if (a.length() == 3) {
                    stringBuffer.append("-");
                }
            }

            if (b != null && b.length() > 0) {
                stringBuffer.append(b);
                if (b.length() == 4) {
                    stringBuffer.append("-");
                }
            }
            if (c != null && c.length() > 0) {
                stringBuffer.append(c);
                if (c.length() == 7) {
                    stringBuffer.append("-");
                }
            }

            if (d != null && d.length() > 0) {
                stringBuffer.append(d);
            }
            editText.removeTextChangedListener(textWatcher);
            editText.setText(stringBuffer.toString());
            editText.setSelection(editText.getText().length());
            editText.addTextChangedListener(textWatcher);
        } else {
            editText.removeTextChangedListener(textWatcher);
            editText.setText("");
            editText.addTextChangedListener(textWatcher);
        }

    }
}