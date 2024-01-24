package mainApp;

import java.sql.Time;

/**
 * Converts given strings into the correct format to be used for a given data
 * type
 * 
 * @author beaslebf
 *
 */
public class DataTypeConverter {

	String toConvert;

	public DataTypeConverter() {
	}
	
	/**
	 * converts the decimal to a string time
	 * @param time as decimal
	 * @return string
	 */
	public String decimalToString(Double time) {
		String timeString = time.toString();
		StringBuilder sb = new StringBuilder();
		String seconds = null;
		if(time >= 0) {
			seconds = timeString.substring(0, timeString.indexOf('.'));
		}else {
			seconds = timeString.substring(1, timeString.indexOf('.'));
			sb.append('-');
		}
		
		Integer minutes = Integer.parseInt(seconds) / 60;
		Integer sec = Integer.parseInt(seconds) - (minutes * 60);
		if(minutes > 0) {
			sb.append(minutes.toString());
			sb.append(":");
		}
		if(sec < 10) {
			sb.append("0");
		}
		sb.append(sec.toString());
		String ms = timeString.substring(timeString.indexOf('.'));
		if(ms.length() < 3) {
			sb.append(ms + '0');
		}else {
			sb.append(ms);
		}
		return sb.toString();
	}
	
	/**
	 * returns the time as a double of seconds and milliseconds
	 * @param string
	 * @return double
	 */
	public double stringToDecimal(String string) {
		Double seconds = 0.0;
		if (string.indexOf(':') > -1) {
			String minutes = string.substring(0, string.indexOf(':'));
			seconds += (double)60 * Integer.parseInt(minutes);
			String sec = string.substring(string.indexOf(':') + 1, string.indexOf('.'));
			seconds += (double)Integer.parseInt(sec);
			String ms = "0." + string.substring(string.indexOf('.') + 1);
			seconds += Double.parseDouble(ms);
		}else {
			return Double.parseDouble(string);
		}

		return seconds;
	}
}
