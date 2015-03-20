package com.qian.down;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

public class DownloadContext {
	private HttpServletResponse response = null;
	public static final String CONTENT_TYPE_DOWNLOAD = "application/octet-stream";

	public static final String DEFAULT_FILENAME_ENCODING = "ISO-8859-1";

	public static final String RESPONSE_HEAD_NAME = "Content-disposition";

	public static final String RESPONSE_HEAD_VALUE = "attachment;filename=";
	private DownloadContext(HttpServletResponse response) {
		this.response = response;
	}
	public static DownloadContext newInstance(HttpServletResponse response) {
		return new DownloadContext(response);
	}
	public void download(String filename, InputStream in) {
		try {
			response.reset();
			response.setContentType(CONTENT_TYPE_DOWNLOAD);
			setDownloadHeader(response, filename);
			copy(in, response.getOutputStream(),
					new byte[response.getBufferSize()]);
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
		} catch (Throwable e) {
		}
	}
	

	public static final void setDownloadHeader(HttpServletResponse response,
			String filename) {
		try {
			filename = new String(filename.getBytes(), DEFAULT_FILENAME_ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		}
		response.setHeader(RESPONSE_HEAD_NAME,
				new StringBuffer(RESPONSE_HEAD_VALUE.length()
						+ filename.length()).append(RESPONSE_HEAD_VALUE)
						.append(filename).toString());
	}

	public static void copy(InputStream in, OutputStream out, byte[] buffer) {
		try {
			for (int i = 0; (i = in.read(buffer)) != -1;) {
				out.write(buffer, 0, i);
				out.flush();
			}
		} catch (Throwable e) {

		} finally {
			try {
				if (in != null)	in.close();
				if (out != null)out.close();
			} catch (IOException e) {
			
			}
		}
	}


}
