package preprocessamento;

import org.apache.commons.lang.StringUtils;

public class HeaderDetector {
	
	private String header = null;
	private String text = null;
	private int headerOccurrence = 0;
	private int minSnipSize = 10;
	private int maxSnipSize = 300;
	
	public void detectHeader(String text) {
		
		this.text = text;
		int max, min;
		
		if(maxSnipSize >= text.length()) {
			max = text.length() / 4;
			min = max / 2;
		}
		else {
			max = maxSnipSize;
			min = minSnipSize;
		}
		
		String snip     = "";
		String leftover = "";
		
		for(int i=min; i<=max; i++) {
			snip     = text.substring(0, i);
			leftover = text.substring(i);
			
			if (!leftover.contains(snip)) {
				snip = text.substring(0, i-1);
				break;
			}
		}
		
		if(snip.length() > min)
			header = snip;

		headerOccurrence = StringUtils.countMatches(text, header);
	}

	public String removeHeader() {
		return header != null ? text.replace(header, "") : text;
	}
	
	
	
	public String getHeader() {
		return header;
	}

	public String getText() {
		return text;
	}

	public int getHeaderOccurrence() {
		return headerOccurrence;
	}

	public int getMinSnipSize() {
		return minSnipSize;
	}

	public void setMinSnipSize(int minSnipSize) {
		this.minSnipSize = minSnipSize;
	}

	public int getMaxSnipSize() {
		return maxSnipSize;
	}

	public void setMaxSnipSize(int maxSnipSize) {
		this.maxSnipSize = maxSnipSize;
	}

}
