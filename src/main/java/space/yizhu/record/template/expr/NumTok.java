

package space.yizhu.record.template.expr;

import java.math.BigDecimal;

import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;


public class NumTok extends Tok {

    private Number value;

    NumTok(Sym sym, String s, int radix, boolean isScientificNotation, Location location) {
        super(sym, location.getRow());
        try {
            typeConvert(sym, s, radix, isScientificNotation, location);
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), location, e);
        }
    }

    private void typeConvert(Sym sym, String s, int radix, boolean isScientificNotation, Location location) {
        switch (sym) {
            case INT:
                if (isScientificNotation) {
                    value = new BigDecimal(s).intValue();
                } else {
                    value = Integer.valueOf(s, radix);        
                }
                break;
            case LONG:
                if (isScientificNotation) {
                    value = new BigDecimal(s).longValue();
                } else {
                    value = Long.valueOf(s, radix);            
                }
                break;
            case FLOAT:
                if (isScientificNotation) {
                    value = new BigDecimal(s).floatValue();
                } else {
                    value = Float.valueOf(s);                
                }
                break;
            case DOUBLE:
                if (isScientificNotation) {
                    value = new BigDecimal(s).doubleValue();
                } else {
                    value = Double.valueOf(s);                
                }
                break;
            default:
                throw new ParseException("Unsupported type: " + sym.value(), location);
        }
    }

    public String value() {
        return value.toString();
    }

    public Object getNumberValue() {
        return value;
    }

    public String toString() {
        return sym.value() + " : " + value;
    }
}





