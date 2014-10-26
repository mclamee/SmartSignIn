package com.ssi.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
   public class SortableProperties extends Properties{
        private static final long serialVersionUID = -3551491043545568685L;
        private Comparator<String> keyCompartaor;
        
        public SortableProperties() {
            setKeyCompartaor(new Comparator<String>() {
                @Override
                public int compare(String k1, String k2) {
                    return k1.compareTo(k2);
                }
            });
        }
        
        public SortableProperties(Comparator<String> keyCompartaor) {
            this.setKeyCompartaor(keyCompartaor);
        }
        
        /**
         * Writes this property list (key and element pairs) in this
         * <code>Properties</code> table to the output stream in a format suitable
         * for loading into a <code>Properties</code> table using the
         * {@link #load(InputStream) load(InputStream)} method.
         * <p>
         * Properties from the defaults table of this <code>Properties</code>
         * table (if any) are <i>not</i> written out by this method.
         * <p>
         * This method outputs the comments, properties keys and values in
         * the same format as specified in
         * {@link #store(java.io.Writer, java.lang.String) store(Writer)},
         * with the following differences:
         * <ul>
         * <li>The stream is written using the ISO 8859-1 character encoding.
         *
         * <li>Characters not in Latin-1 in the comments are written as
         * <code>&#92;u</code><i>xxxx</i> for their appropriate unicode
         * hexadecimal value <i>xxxx</i>.
         *
         * <li>Characters less than <code>&#92;u0020</code> and characters greater
         * than <code>&#92;u007E</code> in property keys or values are written
         * as <code>&#92;u</code><i>xxxx</i> for the appropriate hexadecimal
         * value <i>xxxx</i>.
         * </ul>
         * <p>
         * After the entries have been written, the output stream is flushed.
         * The output stream remains open after this method returns.
         * <p>
         * @param propFileToStore 
         * @param   out      an output stream.
         * @param   comments   a description of the property list.
         * @exception  IOException if writing this property list to the specified
         *             output stream throws an <tt>IOException</tt>.
         * @exception  ClassCastException  if this <code>Properties</code> object
         *             contains any keys or values that are not <code>Strings</code>.
         * @exception  NullPointerException  if <code>out</code> is null.
         * @since 1.2
         */
        @Override
		public void store(OutputStream out, String comments)
                throws IOException
            {
                store0(new BufferedWriter(new OutputStreamWriter(out, "8859_1")),
                       comments,
                       true);
            }

            private void store0(BufferedWriter bw, String comments, boolean escUnicode)
                throws IOException
            {
                if (comments != null) {
                    writeComments(bw, comments);
                }
                bw.write("#" + new Date().toString());
                bw.newLine();
                synchronized (this) {
                    
                    ArrayList<String> keyList = new ArrayList<String>();
                    for (Enumeration e = keys(); e.hasMoreElements();) {
                        String key = (String)e.nextElement();
                        keyList.add(key);
                    }
                    
                    Collections.sort(keyList, this.getKeyCompartaor());
                    
                    for (String key: keyList) {
                        String val = (String)get(key);
                        key = saveConvert(key, true, escUnicode);
                        /* No need to escape embedded and trailing spaces for value, hence
                         * pass false to flag.
                         */
                        val = saveConvert(val, false, escUnicode);
                        bw.write(key + "=" + val);
                        bw.newLine();
                    }
                }
                bw.flush();
            }
        
        /*
         * Converts unicodes to encoded &#92;uxxxx and escapes
         * special characters with a preceding slash
         */
        private String saveConvert(String theString,
                                   boolean escapeSpace,
                                   boolean escapeUnicode) {
            int len = theString.length();
            int bufLen = len * 2;
            if (bufLen < 0) {
                bufLen = Integer.MAX_VALUE;
            }
            StringBuffer outBuffer = new StringBuffer(bufLen);

            for(int x=0; x<len; x++) {
                char aChar = theString.charAt(x);
                // Handle common case first, selecting largest block that
                // avoids the specials below
                if ((aChar > 61) && (aChar < 127)) {
                    if (aChar == '\\') {
                        outBuffer.append('\\'); outBuffer.append('\\');
                        continue;
                    }
                    outBuffer.append(aChar);
                    continue;
                }
                switch(aChar) {
                    case ' ':
                        if (x == 0 || escapeSpace)
                            outBuffer.append('\\');
                        outBuffer.append(' ');
                        break;
                    case '\t':outBuffer.append('\\'); outBuffer.append('t');
                              break;
                    case '\n':outBuffer.append('\\'); outBuffer.append('n');
                              break;
                    case '\r':outBuffer.append('\\'); outBuffer.append('r');
                              break;
                    case '\f':outBuffer.append('\\'); outBuffer.append('f');
                              break;
                    case '=': // Fall through
                    case ':': // Fall through
                    case '#': // Fall through
                    case '!':
                        outBuffer.append('\\'); outBuffer.append(aChar);
                        break;
                    default:
                        if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode ) {
                            outBuffer.append('\\');
                            outBuffer.append('u');
                            outBuffer.append(toHex((aChar >> 12) & 0xF));
                            outBuffer.append(toHex((aChar >>  8) & 0xF));
                            outBuffer.append(toHex((aChar >>  4) & 0xF));
                            outBuffer.append(toHex( aChar        & 0xF));
                        } else {
                            outBuffer.append(aChar);
                        }
                }
            }
            return outBuffer.toString();
        }
        
        private void writeComments(BufferedWriter bw, String comments)
                throws IOException {
                bw.write("#");
                int len = comments.length();
                int current = 0;
                int last = 0;
                char[] uu = new char[6];
                uu[0] = '\\';
                uu[1] = 'u';
                while (current < len) {
                    char c = comments.charAt(current);
                    if (c > '\u00ff' || c == '\n' || c == '\r') {
                        if (last != current)
                            bw.write(comments.substring(last, current));
                        if (c > '\u00ff') {
                            uu[2] = toHex((c >> 12) & 0xf);
                            uu[3] = toHex((c >>  8) & 0xf);
                            uu[4] = toHex((c >>  4) & 0xf);
                            uu[5] = toHex( c        & 0xf);
                            bw.write(new String(uu));
                        } else {
                            bw.newLine();
                            if (c == '\r' &&
                                current != len - 1 &&
                                comments.charAt(current + 1) == '\n') {
                                current++;
                            }
                            if (current == len - 1 ||
                                (comments.charAt(current + 1) != '#' &&
                                comments.charAt(current + 1) != '!'))
                                bw.write("#");
                        }
                        last = current + 1;
                    }
                    current++;
                }
                if (last != current)
                    bw.write(comments.substring(last, current));
                bw.newLine();
            }
        
        /**
         * Convert a nibble to a hex character
         * @param   nibble  the nibble to convert.
         */
        private char toHex(int nibble) {
            return hexDigit[(nibble & 0xF)];
        }

        /**
         * @return the keyCompartaor
         */
        public Comparator<String> getKeyCompartaor() {
            return keyCompartaor;
        }

        /**
         * @param keyCompartaor the keyCompartaor to set
         */
        public void setKeyCompartaor(Comparator<String> keyCompartaor) {
            this.keyCompartaor = keyCompartaor;
        }

        /** A table of hex digits */
        private final char[] hexDigit = {
            '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
        };
    }  