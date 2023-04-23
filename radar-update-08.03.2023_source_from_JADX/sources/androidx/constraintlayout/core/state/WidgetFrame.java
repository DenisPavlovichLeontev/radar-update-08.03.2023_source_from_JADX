package androidx.constraintlayout.core.state;

import androidx.constraintlayout.core.motion.CustomVariable;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.constraintlayout.core.parser.CLElement;
import androidx.constraintlayout.core.parser.CLKey;
import androidx.constraintlayout.core.parser.CLNumber;
import androidx.constraintlayout.core.parser.CLObject;
import androidx.constraintlayout.core.parser.CLParsingException;
import androidx.constraintlayout.core.state.Transition;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import java.util.HashMap;
import java.util.Set;

public class WidgetFrame {
    private static final boolean OLD_SYSTEM = true;
    public static float phone_orientation = Float.NaN;
    public float alpha = Float.NaN;
    public int bottom = 0;
    public float interpolatedPos = Float.NaN;
    public int left = 0;
    public final HashMap<String, CustomVariable> mCustom = new HashMap<>();
    public String name = null;
    public float pivotX = Float.NaN;
    public float pivotY = Float.NaN;
    public int right = 0;
    public float rotationX = Float.NaN;
    public float rotationY = Float.NaN;
    public float rotationZ = Float.NaN;
    public float scaleX = Float.NaN;
    public float scaleY = Float.NaN;
    public int top = 0;
    public float translationX = Float.NaN;
    public float translationY = Float.NaN;
    public float translationZ = Float.NaN;
    public int visibility = 0;
    public ConstraintWidget widget = null;

    public int width() {
        return Math.max(0, this.right - this.left);
    }

    public int height() {
        return Math.max(0, this.bottom - this.top);
    }

    public WidgetFrame() {
    }

    public WidgetFrame(ConstraintWidget constraintWidget) {
        this.widget = constraintWidget;
    }

    public WidgetFrame(WidgetFrame widgetFrame) {
        this.widget = widgetFrame.widget;
        this.left = widgetFrame.left;
        this.top = widgetFrame.top;
        this.right = widgetFrame.right;
        this.bottom = widgetFrame.bottom;
        updateAttributes(widgetFrame);
    }

    public void updateAttributes(WidgetFrame widgetFrame) {
        this.pivotX = widgetFrame.pivotX;
        this.pivotY = widgetFrame.pivotY;
        this.rotationX = widgetFrame.rotationX;
        this.rotationY = widgetFrame.rotationY;
        this.rotationZ = widgetFrame.rotationZ;
        this.translationX = widgetFrame.translationX;
        this.translationY = widgetFrame.translationY;
        this.translationZ = widgetFrame.translationZ;
        this.scaleX = widgetFrame.scaleX;
        this.scaleY = widgetFrame.scaleY;
        this.alpha = widgetFrame.alpha;
        this.visibility = widgetFrame.visibility;
        this.mCustom.clear();
        if (widgetFrame != null) {
            for (CustomVariable next : widgetFrame.mCustom.values()) {
                this.mCustom.put(next.getName(), next.copy());
            }
        }
    }

    public boolean isDefaultTransform() {
        return Float.isNaN(this.rotationX) && Float.isNaN(this.rotationY) && Float.isNaN(this.rotationZ) && Float.isNaN(this.translationX) && Float.isNaN(this.translationY) && Float.isNaN(this.translationZ) && Float.isNaN(this.scaleX) && Float.isNaN(this.scaleY) && Float.isNaN(this.alpha);
    }

    public static void interpolate(int i, int i2, WidgetFrame widgetFrame, WidgetFrame widgetFrame2, WidgetFrame widgetFrame3, Transition transition, float f) {
        float f2;
        int i3;
        int i4;
        int i5;
        WidgetFrame widgetFrame4;
        float f3;
        int i6;
        int i7;
        int i8;
        int i9 = i;
        int i10 = i2;
        WidgetFrame widgetFrame5 = widgetFrame;
        WidgetFrame widgetFrame6 = widgetFrame2;
        WidgetFrame widgetFrame7 = widgetFrame3;
        Transition transition2 = transition;
        float f4 = 100.0f * f;
        int i11 = (int) f4;
        int i12 = widgetFrame6.left;
        int i13 = widgetFrame6.top;
        int i14 = widgetFrame7.left;
        int i15 = widgetFrame7.top;
        int i16 = widgetFrame7.right - i14;
        int i17 = widgetFrame6.right - i12;
        int i18 = widgetFrame7.bottom - i15;
        int i19 = widgetFrame6.bottom - i13;
        float f5 = widgetFrame6.alpha;
        float f6 = widgetFrame7.alpha;
        float f7 = f4;
        if (widgetFrame6.visibility == 8) {
            i12 = (int) (((float) i12) - (((float) i16) / 2.0f));
            i13 = (int) (((float) i13) - (((float) i18) / 2.0f));
            if (Float.isNaN(f5)) {
                i3 = i18;
                i4 = i16;
                f2 = 0.0f;
            } else {
                f2 = f5;
                i4 = i16;
                i3 = i18;
            }
        } else {
            i4 = i17;
            f2 = f5;
            i3 = i19;
        }
        if (widgetFrame7.visibility == 8) {
            i14 = (int) (((float) i14) - (((float) i4) / 2.0f));
            i15 = (int) (((float) i15) - (((float) i3) / 2.0f));
            i16 = i4;
            i18 = i3;
            if (Float.isNaN(f6)) {
                f6 = 0.0f;
            }
        }
        float f8 = (!Float.isNaN(f2) || Float.isNaN(f6)) ? f2 : 1.0f;
        if (!Float.isNaN(f8) && Float.isNaN(f6)) {
            f6 = 1.0f;
        }
        if (widgetFrame5.widget == null || !transition.hasPositionKeyframes()) {
            widgetFrame4 = widgetFrame2;
            f3 = f;
            i5 = i12;
        } else {
            Transition.KeyPosition findPreviousPosition = transition2.findPreviousPosition(widgetFrame5.widget.stringId, i11);
            int i20 = i12;
            Transition.KeyPosition findNextPosition = transition2.findNextPosition(widgetFrame5.widget.stringId, i11);
            if (findPreviousPosition == findNextPosition) {
                findNextPosition = null;
            }
            int i21 = 0;
            if (findPreviousPosition != null) {
                i7 = i2;
                i6 = (int) (findPreviousPosition.f35x * ((float) i9));
                i13 = (int) (findPreviousPosition.f36y * ((float) i7));
                i21 = findPreviousPosition.frame;
            } else {
                i7 = i2;
                i6 = i20;
            }
            if (findNextPosition != null) {
                int i22 = (int) (findNextPosition.f35x * ((float) i9));
                i8 = findNextPosition.frame;
                i14 = i22;
                i15 = (int) (findNextPosition.f36y * ((float) i7));
            } else {
                i8 = 100;
            }
            f3 = (f7 - ((float) i21)) / ((float) (i8 - i21));
            widgetFrame4 = widgetFrame2;
            i5 = i6;
        }
        widgetFrame5.widget = widgetFrame4.widget;
        int i23 = (int) (((float) i5) + (((float) (i14 - i5)) * f3));
        widgetFrame5.left = i23;
        int i24 = (int) (((float) i13) + (f3 * ((float) (i15 - i13))));
        widgetFrame5.top = i24;
        float f9 = f;
        float f10 = 1.0f - f9;
        widgetFrame5.right = i23 + ((int) ((((float) i4) * f10) + (((float) i16) * f9)));
        widgetFrame5.bottom = i24 + ((int) ((f10 * ((float) i3)) + (((float) i18) * f9)));
        widgetFrame5.pivotX = interpolate(widgetFrame4.pivotX, widgetFrame7.pivotX, 0.5f, f9);
        widgetFrame5.pivotY = interpolate(widgetFrame4.pivotY, widgetFrame7.pivotY, 0.5f, f9);
        widgetFrame5.rotationX = interpolate(widgetFrame4.rotationX, widgetFrame7.rotationX, 0.0f, f9);
        widgetFrame5.rotationY = interpolate(widgetFrame4.rotationY, widgetFrame7.rotationY, 0.0f, f9);
        widgetFrame5.rotationZ = interpolate(widgetFrame4.rotationZ, widgetFrame7.rotationZ, 0.0f, f9);
        widgetFrame5.scaleX = interpolate(widgetFrame4.scaleX, widgetFrame7.scaleX, 1.0f, f9);
        widgetFrame5.scaleY = interpolate(widgetFrame4.scaleY, widgetFrame7.scaleY, 1.0f, f9);
        widgetFrame5.translationX = interpolate(widgetFrame4.translationX, widgetFrame7.translationX, 0.0f, f9);
        widgetFrame5.translationY = interpolate(widgetFrame4.translationY, widgetFrame7.translationY, 0.0f, f9);
        widgetFrame5.translationZ = interpolate(widgetFrame4.translationZ, widgetFrame7.translationZ, 0.0f, f9);
        widgetFrame5.alpha = interpolate(f8, f6, 1.0f, f9);
    }

    private static float interpolate(float f, float f2, float f3, float f4) {
        boolean isNaN = Float.isNaN(f);
        boolean isNaN2 = Float.isNaN(f2);
        if (isNaN && isNaN2) {
            return Float.NaN;
        }
        if (isNaN) {
            f = f3;
        }
        if (isNaN2) {
            f2 = f3;
        }
        return f + (f4 * (f2 - f));
    }

    public float centerX() {
        int i = this.left;
        return ((float) i) + (((float) (this.right - i)) / 2.0f);
    }

    public float centerY() {
        int i = this.top;
        return ((float) i) + (((float) (this.bottom - i)) / 2.0f);
    }

    public WidgetFrame update() {
        ConstraintWidget constraintWidget = this.widget;
        if (constraintWidget != null) {
            this.left = constraintWidget.getLeft();
            this.top = this.widget.getTop();
            this.right = this.widget.getRight();
            this.bottom = this.widget.getBottom();
            updateAttributes(this.widget.frame);
        }
        return this;
    }

    public WidgetFrame update(ConstraintWidget constraintWidget) {
        if (constraintWidget == null) {
            return this;
        }
        this.widget = constraintWidget;
        update();
        return this;
    }

    public void addCustomColor(String str, int i) {
        setCustomAttribute(str, (int) TypedValues.Custom.TYPE_COLOR, i);
    }

    public int getCustomColor(String str) {
        if (this.mCustom.containsKey(str)) {
            return this.mCustom.get(str).getColorValue();
        }
        return -21880;
    }

    public void addCustomFloat(String str, float f) {
        setCustomAttribute(str, (int) TypedValues.Custom.TYPE_FLOAT, f);
    }

    public float getCustomFloat(String str) {
        if (this.mCustom.containsKey(str)) {
            return this.mCustom.get(str).getFloatValue();
        }
        return Float.NaN;
    }

    public void setCustomAttribute(String str, int i, float f) {
        if (this.mCustom.containsKey(str)) {
            this.mCustom.get(str).setFloatValue(f);
        } else {
            this.mCustom.put(str, new CustomVariable(str, i, f));
        }
    }

    public void setCustomAttribute(String str, int i, int i2) {
        if (this.mCustom.containsKey(str)) {
            this.mCustom.get(str).setIntValue(i2);
        } else {
            this.mCustom.put(str, new CustomVariable(str, i, i2));
        }
    }

    public void setCustomAttribute(String str, int i, boolean z) {
        if (this.mCustom.containsKey(str)) {
            this.mCustom.get(str).setBooleanValue(z);
        } else {
            this.mCustom.put(str, new CustomVariable(str, i, z));
        }
    }

    public void setCustomAttribute(String str, int i, String str2) {
        if (this.mCustom.containsKey(str)) {
            this.mCustom.get(str).setStringValue(str2);
        } else {
            this.mCustom.put(str, new CustomVariable(str, i, str2));
        }
    }

    public CustomVariable getCustomAttribute(String str) {
        return this.mCustom.get(str);
    }

    public Set<String> getCustomAttributeNames() {
        return this.mCustom.keySet();
    }

    public boolean setValue(String str, CLElement cLElement) throws CLParsingException {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1881940865:
                if (str.equals("phone_orientation")) {
                    c = 0;
                    break;
                }
                break;
            case -1383228885:
                if (str.equals("bottom")) {
                    c = 1;
                    break;
                }
                break;
            case -1349088399:
                if (str.equals("custom")) {
                    c = 2;
                    break;
                }
                break;
            case -1249320806:
                if (str.equals("rotationX")) {
                    c = 3;
                    break;
                }
                break;
            case -1249320805:
                if (str.equals("rotationY")) {
                    c = 4;
                    break;
                }
                break;
            case -1249320804:
                if (str.equals("rotationZ")) {
                    c = 5;
                    break;
                }
                break;
            case -1225497657:
                if (str.equals("translationX")) {
                    c = 6;
                    break;
                }
                break;
            case -1225497656:
                if (str.equals("translationY")) {
                    c = 7;
                    break;
                }
                break;
            case -1225497655:
                if (str.equals("translationZ")) {
                    c = 8;
                    break;
                }
                break;
            case -987906986:
                if (str.equals("pivotX")) {
                    c = 9;
                    break;
                }
                break;
            case -987906985:
                if (str.equals("pivotY")) {
                    c = 10;
                    break;
                }
                break;
            case -908189618:
                if (str.equals("scaleX")) {
                    c = 11;
                    break;
                }
                break;
            case -908189617:
                if (str.equals("scaleY")) {
                    c = 12;
                    break;
                }
                break;
            case 115029:
                if (str.equals("top")) {
                    c = 13;
                    break;
                }
                break;
            case 3317767:
                if (str.equals("left")) {
                    c = 14;
                    break;
                }
                break;
            case 92909918:
                if (str.equals("alpha")) {
                    c = 15;
                    break;
                }
                break;
            case 108511772:
                if (str.equals("right")) {
                    c = 16;
                    break;
                }
                break;
            case 642850769:
                if (str.equals("interpolatedPos")) {
                    c = 17;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                phone_orientation = cLElement.getFloat();
                break;
            case 1:
                this.bottom = cLElement.getInt();
                break;
            case 2:
                parseCustom(cLElement);
                break;
            case 3:
                this.rotationX = cLElement.getFloat();
                break;
            case 4:
                this.rotationY = cLElement.getFloat();
                break;
            case 5:
                this.rotationZ = cLElement.getFloat();
                break;
            case 6:
                this.translationX = cLElement.getFloat();
                break;
            case 7:
                this.translationY = cLElement.getFloat();
                break;
            case 8:
                this.translationZ = cLElement.getFloat();
                break;
            case 9:
                this.pivotX = cLElement.getFloat();
                break;
            case 10:
                this.pivotY = cLElement.getFloat();
                break;
            case 11:
                this.scaleX = cLElement.getFloat();
                break;
            case 12:
                this.scaleY = cLElement.getFloat();
                break;
            case 13:
                this.top = cLElement.getInt();
                break;
            case 14:
                this.left = cLElement.getInt();
                break;
            case 15:
                this.alpha = cLElement.getFloat();
                break;
            case 16:
                this.right = cLElement.getInt();
                break;
            case 17:
                this.interpolatedPos = cLElement.getFloat();
                break;
            default:
                return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void parseCustom(CLElement cLElement) throws CLParsingException {
        CLObject cLObject = (CLObject) cLElement;
        int size = cLObject.size();
        for (int i = 0; i < size; i++) {
            CLKey cLKey = (CLKey) cLObject.get(i);
            cLKey.content();
            CLElement value = cLKey.getValue();
            String content = value.content();
            if (content.matches("#[0-9a-fA-F]+")) {
                setCustomAttribute(cLKey.content(), (int) TypedValues.Custom.TYPE_COLOR, Integer.parseInt(content.substring(1), 16));
            } else if (value instanceof CLNumber) {
                setCustomAttribute(cLKey.content(), (int) TypedValues.Custom.TYPE_FLOAT, value.getFloat());
            } else {
                setCustomAttribute(cLKey.content(), (int) TypedValues.Custom.TYPE_STRING, content);
            }
        }
    }

    public StringBuilder serialize(StringBuilder sb) {
        return serialize(sb, false);
    }

    public StringBuilder serialize(StringBuilder sb, boolean z) {
        sb.append("{\n");
        add(sb, "left", this.left);
        add(sb, "top", this.top);
        add(sb, "right", this.right);
        add(sb, "bottom", this.bottom);
        add(sb, "pivotX", this.pivotX);
        add(sb, "pivotY", this.pivotY);
        add(sb, "rotationX", this.rotationX);
        add(sb, "rotationY", this.rotationY);
        add(sb, "rotationZ", this.rotationZ);
        add(sb, "translationX", this.translationX);
        add(sb, "translationY", this.translationY);
        add(sb, "translationZ", this.translationZ);
        add(sb, "scaleX", this.scaleX);
        add(sb, "scaleY", this.scaleY);
        add(sb, "alpha", this.alpha);
        add(sb, "visibility", this.left);
        add(sb, "interpolatedPos", this.interpolatedPos);
        if (z) {
            add(sb, "phone_orientation", phone_orientation);
        }
        if (z) {
            add(sb, "phone_orientation", phone_orientation);
        }
        if (this.mCustom.size() != 0) {
            sb.append("custom : {\n");
            for (String next : this.mCustom.keySet()) {
                CustomVariable customVariable = this.mCustom.get(next);
                sb.append(next);
                sb.append(": ");
                switch (customVariable.getType()) {
                    case TypedValues.Custom.TYPE_INT:
                        sb.append(customVariable.getIntegerValue());
                        sb.append(",\n");
                        break;
                    case TypedValues.Custom.TYPE_FLOAT:
                    case TypedValues.Custom.TYPE_DIMENSION:
                        sb.append(customVariable.getFloatValue());
                        sb.append(",\n");
                        break;
                    case TypedValues.Custom.TYPE_COLOR:
                        sb.append("'");
                        sb.append(CustomVariable.colorString(customVariable.getIntegerValue()));
                        sb.append("',\n");
                        break;
                    case TypedValues.Custom.TYPE_STRING:
                        sb.append("'");
                        sb.append(customVariable.getStringValue());
                        sb.append("',\n");
                        break;
                    case TypedValues.Custom.TYPE_BOOLEAN:
                        sb.append("'");
                        sb.append(customVariable.getBooleanValue());
                        sb.append("',\n");
                        break;
                }
            }
            sb.append("}\n");
        }
        sb.append("}\n");
        return sb;
    }

    private static void add(StringBuilder sb, String str, int i) {
        sb.append(str);
        sb.append(": ");
        sb.append(i);
        sb.append(",\n");
    }

    private static void add(StringBuilder sb, String str, float f) {
        if (!Float.isNaN(f)) {
            sb.append(str);
            sb.append(": ");
            sb.append(f);
            sb.append(",\n");
        }
    }

    /* access modifiers changed from: package-private */
    public void printCustomAttributes() {
        String str;
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str2 = (".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName()) + " " + (hashCode() % 1000);
        if (this.widget != null) {
            str = str2 + "/" + (this.widget.hashCode() % 1000) + " ";
        } else {
            str = str2 + "/NULL ";
        }
        HashMap<String, CustomVariable> hashMap = this.mCustom;
        if (hashMap != null) {
            for (String str3 : hashMap.keySet()) {
                System.out.println(str + this.mCustom.get(str3).toString());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void logv(String str) {
        String str2;
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[1];
        String str3 = (".(" + stackTraceElement.getFileName() + ":" + stackTraceElement.getLineNumber() + ") " + stackTraceElement.getMethodName()) + " " + (hashCode() % 1000);
        if (this.widget != null) {
            str2 = str3 + "/" + (this.widget.hashCode() % 1000);
        } else {
            str2 = str3 + "/NULL";
        }
        System.out.println(str2 + " " + str);
    }
}
