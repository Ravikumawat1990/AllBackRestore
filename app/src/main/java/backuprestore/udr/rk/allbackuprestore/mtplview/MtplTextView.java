package backuprestore.udr.rk.allbackuprestore.mtplview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import backuprestore.udr.rk.allbackuprestore.util.CM;


public class MtplTextView extends TextView {

    Context mContext;

    public MtplTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        if (!isInEditMode())
            initAttributes(attrs);
    }

    public MtplTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        if (!isInEditMode())
            initAttributes(attrs);
    }

    public MtplTextView(Context context) {
        super(context);
        this.mContext = context;
        if (!isInEditMode())
            initAttributes(null);
    }

    private void initAttributes(AttributeSet attrs) {

        Typeface typeFace = CM.getTypeFace(mContext, attrs);
        if (typeFace != null) {
            setTypeface(typeFace);
        }
    }

}
