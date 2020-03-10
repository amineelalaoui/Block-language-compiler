/* The following code was generated by JFlex 1.3.5 on 2/19/19 6:47 PM */

package fr.n7.stl.block;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;
import java_cup.runtime.Symbol;

import java.io.IOException;
import java.lang.*;
import java.io.InputStreamReader;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.3.5
 * on 2/19/19 6:47 PM from the specification file
 * <tt>file:/Users/marc/Eclipse-Work-Space/eclipse-workspace-oxygen-1/fr.n7.stl.block/lexer.jflex</tt>
 */
public class Lexer implements sym, java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  final public static int YYEOF = -1;

  /** initial size of the lookahead buffer */
  final private static int YY_BUFFERSIZE = 16384;

  /** lexical states */
  final public static int CODESEG = 1;
  final public static int YYINITIAL = 0;

  /** 
   * Translates characters to character classes
   */
  final private static String yycmap_packed = 
    "\11\15\1\3\1\2\1\0\1\3\1\1\16\15\4\0\1\3\1\22"+
    "\1\11\1\0\1\14\1\30\1\32\1\13\1\33\1\34\1\10\1\26"+
    "\1\17\1\27\1\6\1\7\1\4\11\5\1\20\1\16\1\24\1\23"+
    "\1\25\1\21\1\0\22\14\1\62\7\14\1\37\1\12\1\40\1\0"+
    "\1\14\1\0\1\60\1\64\1\41\1\51\1\50\1\52\1\63\1\61"+
    "\1\56\2\14\1\57\1\55\1\43\1\42\1\47\1\14\1\53\1\44"+
    "\1\45\1\54\1\65\1\66\1\14\1\46\1\14\1\35\1\31\1\36"+
    "\1\0\41\15\2\0\4\14\4\0\1\14\2\0\1\15\7\0\1\14"+
    "\4\0\1\14\5\0\27\14\1\0\37\14\1\0\u01ca\14\4\0\14\14"+
    "\16\0\5\14\7\0\1\14\1\0\1\14\21\0\160\15\5\14\1\0"+
    "\2\14\2\0\4\14\10\0\1\14\1\0\3\14\1\0\1\14\1\0"+
    "\24\14\1\0\123\14\1\0\213\14\1\0\5\15\2\0\236\14\11\0"+
    "\46\14\2\0\1\14\7\0\47\14\7\0\1\14\1\0\55\15\1\0"+
    "\1\15\1\0\2\15\1\0\2\15\1\0\1\15\10\0\33\14\5\0"+
    "\3\14\15\0\5\15\6\0\1\14\4\0\13\15\5\0\53\14\37\15"+
    "\4\0\2\14\1\15\143\14\1\0\1\14\10\15\1\0\6\15\2\14"+
    "\2\15\1\0\4\15\2\14\12\15\3\14\2\0\1\14\17\0\1\15"+
    "\1\14\1\15\36\14\33\15\2\0\131\14\13\15\1\14\16\0\12\15"+
    "\41\14\11\15\2\14\4\0\1\14\5\0\26\14\4\15\1\14\11\15"+
    "\1\14\3\15\1\14\5\15\22\0\31\14\3\15\104\0\1\14\1\0"+
    "\13\14\67\0\33\15\1\0\4\15\66\14\3\15\1\14\22\15\1\14"+
    "\7\15\12\14\2\15\2\0\12\15\1\0\7\14\1\0\7\14\1\0"+
    "\3\15\1\0\10\14\2\0\2\14\2\0\26\14\1\0\7\14\1\0"+
    "\1\14\3\0\4\14\2\0\1\15\1\14\7\15\2\0\2\15\2\0"+
    "\3\15\1\14\10\0\1\15\4\0\2\14\1\0\3\14\2\15\2\0"+
    "\12\15\4\14\7\0\1\14\5\0\3\15\1\0\6\14\4\0\2\14"+
    "\2\0\26\14\1\0\7\14\1\0\2\14\1\0\2\14\1\0\2\14"+
    "\2\0\1\15\1\0\5\15\4\0\2\15\2\0\3\15\3\0\1\15"+
    "\7\0\4\14\1\0\1\14\7\0\14\15\3\14\1\15\13\0\3\15"+
    "\1\0\11\14\1\0\3\14\1\0\26\14\1\0\7\14\1\0\2\14"+
    "\1\0\5\14\2\0\1\15\1\14\10\15\1\0\3\15\1\0\3\15"+
    "\2\0\1\14\17\0\2\14\2\15\2\0\12\15\1\0\1\14\17\0"+
    "\3\15\1\0\10\14\2\0\2\14\2\0\26\14\1\0\7\14\1\0"+
    "\2\14\1\0\5\14\2\0\1\15\1\14\7\15\2\0\2\15\2\0"+
    "\3\15\10\0\2\15\4\0\2\14\1\0\3\14\2\15\2\0\12\15"+
    "\1\0\1\14\20\0\1\15\1\14\1\0\6\14\3\0\3\14\1\0"+
    "\4\14\3\0\2\14\1\0\1\14\1\0\2\14\3\0\2\14\3\0"+
    "\3\14\3\0\14\14\4\0\5\15\3\0\3\15\1\0\4\15\2\0"+
    "\1\14\6\0\1\15\16\0\12\15\11\0\1\14\7\0\3\15\1\0"+
    "\10\14\1\0\3\14\1\0\27\14\1\0\12\14\1\0\5\14\3\0"+
    "\1\14\7\15\1\0\3\15\1\0\4\15\7\0\2\15\1\0\2\14"+
    "\6\0\2\14\2\15\2\0\12\15\22\0\2\15\1\0\10\14\1\0"+
    "\3\14\1\0\27\14\1\0\12\14\1\0\5\14\2\0\1\15\1\14"+
    "\7\15\1\0\3\15\1\0\4\15\7\0\2\15\7\0\1\14\1\0"+
    "\2\14\2\15\2\0\12\15\1\0\2\14\17\0\2\15\1\0\10\14"+
    "\1\0\3\14\1\0\51\14\2\0\1\14\7\15\1\0\3\15\1\0"+
    "\4\15\1\14\10\0\1\15\10\0\2\14\2\15\2\0\12\15\12\0"+
    "\6\14\2\0\2\15\1\0\22\14\3\0\30\14\1\0\11\14\1\0"+
    "\1\14\2\0\7\14\3\0\1\15\4\0\6\15\1\0\1\15\1\0"+
    "\10\15\22\0\2\15\15\0\60\14\1\15\2\14\7\15\4\0\10\14"+
    "\10\15\1\0\12\15\47\0\2\14\1\0\1\14\2\0\2\14\1\0"+
    "\1\14\2\0\1\14\6\0\4\14\1\0\7\14\1\0\3\14\1\0"+
    "\1\14\1\0\1\14\2\0\2\14\1\0\4\14\1\15\2\14\6\15"+
    "\1\0\2\15\1\14\2\0\5\14\1\0\1\14\1\0\6\15\2\0"+
    "\12\15\2\0\4\14\40\0\1\14\27\0\2\15\6\0\12\15\13\0"+
    "\1\15\1\0\1\15\1\0\1\15\4\0\2\15\10\14\1\0\44\14"+
    "\4\0\24\15\1\0\2\15\5\14\13\15\1\0\44\15\11\0\1\15"+
    "\71\0\53\14\24\15\1\14\12\15\6\0\6\14\4\15\4\14\3\15"+
    "\1\14\3\15\2\14\7\15\3\14\4\15\15\14\14\15\1\14\17\15"+
    "\2\0\46\14\1\0\1\14\5\0\1\14\2\0\53\14\1\0\u014d\14"+
    "\1\0\4\14\2\0\7\14\1\0\1\14\1\0\4\14\2\0\51\14"+
    "\1\0\4\14\2\0\41\14\1\0\4\14\2\0\7\14\1\0\1\14"+
    "\1\0\4\14\2\0\17\14\1\0\71\14\1\0\4\14\2\0\103\14"+
    "\2\0\3\15\40\0\20\14\20\0\125\14\14\0\u026c\14\2\0\21\14"+
    "\1\0\32\14\5\0\113\14\3\0\3\14\17\0\15\14\1\0\4\14"+
    "\3\15\13\0\22\14\3\15\13\0\22\14\2\15\14\0\15\14\1\0"+
    "\3\14\1\0\2\15\14\0\64\14\40\15\3\0\1\14\3\0\2\14"+
    "\1\15\2\0\12\15\41\0\3\15\2\0\12\15\6\0\130\14\10\0"+
    "\51\14\1\15\1\14\5\0\106\14\12\0\35\14\3\0\14\15\4\0"+
    "\14\15\12\0\12\15\36\14\2\0\5\14\13\0\54\14\4\0\21\15"+
    "\7\14\2\15\6\0\12\15\46\0\27\14\5\15\4\0\65\14\12\15"+
    "\1\0\35\15\2\0\13\15\6\0\12\15\15\0\1\14\130\0\5\15"+
    "\57\14\21\15\7\14\4\0\12\15\21\0\11\15\14\0\3\15\36\14"+
    "\15\15\2\14\12\15\54\14\16\15\14\0\44\14\24\15\10\0\12\15"+
    "\3\0\3\14\12\15\44\14\122\0\3\15\1\0\25\15\4\14\1\15"+
    "\4\14\3\15\2\14\11\0\300\14\47\15\25\0\4\15\u0116\14\2\0"+
    "\6\14\2\0\46\14\2\0\6\14\2\0\10\14\1\0\1\14\1\0"+
    "\1\14\1\0\1\14\1\0\37\14\2\0\65\14\1\0\7\14\1\0"+
    "\1\14\3\0\3\14\1\0\7\14\3\0\4\14\2\0\6\14\4\0"+
    "\15\14\5\0\3\14\1\0\7\14\16\0\5\15\32\0\5\15\20\0"+
    "\2\14\23\0\1\14\13\0\5\15\5\0\6\15\1\0\1\14\15\0"+
    "\1\14\20\0\15\14\3\0\33\14\25\0\15\15\4\0\1\15\3\0"+
    "\14\15\21\0\1\14\4\0\1\14\2\0\12\14\1\0\1\14\3\0"+
    "\5\14\6\0\1\14\1\0\1\14\1\0\1\14\1\0\4\14\1\0"+
    "\13\14\2\0\4\14\5\0\5\14\4\0\1\14\21\0\51\14\u0a77\0"+
    "\57\14\1\0\57\14\1\0\205\14\6\0\4\14\3\15\2\14\14\0"+
    "\46\14\1\0\1\14\5\0\1\14\2\0\70\14\7\0\1\14\17\0"+
    "\1\15\27\14\11\0\7\14\1\0\7\14\1\0\7\14\1\0\7\14"+
    "\1\0\7\14\1\0\7\14\1\0\7\14\1\0\7\14\1\0\40\15"+
    "\57\0\1\14\u01d5\0\3\14\31\0\11\14\6\15\1\0\5\14\2\0"+
    "\5\14\4\0\126\14\2\0\2\15\2\0\3\14\1\0\132\14\1\0"+
    "\4\14\5\0\51\14\3\0\136\14\21\0\33\14\65\0\20\14\u0200\0"+
    "\u19b6\14\112\0\u51cd\14\63\0\u048d\14\103\0\56\14\2\0\u010d\14\3\0"+
    "\20\14\12\15\2\14\24\0\57\14\1\15\4\0\12\15\1\0\31\14"+
    "\7\0\1\15\120\14\2\15\45\0\11\14\2\0\147\14\2\0\4\14"+
    "\1\0\4\14\14\0\13\14\115\0\12\14\1\15\3\14\1\15\4\14"+
    "\1\15\27\14\5\15\20\0\1\14\7\0\64\14\14\0\2\15\62\14"+
    "\21\15\13\0\12\15\6\0\22\15\6\14\3\0\1\14\4\0\12\15"+
    "\34\14\10\15\2\0\27\14\15\15\14\0\35\14\3\0\4\15\57\14"+
    "\16\15\16\0\1\14\12\15\46\0\51\14\16\15\11\0\3\14\1\15"+
    "\10\14\2\15\2\0\12\15\6\0\27\14\3\0\1\14\1\15\4\0"+
    "\60\14\1\15\1\14\3\15\2\14\2\15\5\14\2\15\1\14\1\15"+
    "\1\14\30\0\3\14\2\0\13\14\5\15\2\0\3\14\2\15\12\0"+
    "\6\14\2\0\6\14\2\0\6\14\11\0\7\14\1\0\7\14\221\0"+
    "\43\14\10\15\1\0\2\15\2\0\12\15\6\0\u2ba4\14\14\0\27\14"+
    "\4\0\61\14\u2104\0\u016e\14\2\0\152\14\46\0\7\14\14\0\5\14"+
    "\5\0\1\14\1\15\12\14\1\0\15\14\1\0\5\14\1\0\1\14"+
    "\1\0\2\14\1\0\2\14\1\0\154\14\41\0\u016b\14\22\0\100\14"+
    "\2\0\66\14\50\0\15\14\3\0\20\15\20\0\7\15\14\0\2\14"+
    "\30\0\3\14\31\0\1\14\6\0\5\14\1\0\207\14\2\0\1\15"+
    "\4\0\1\14\13\0\12\15\7\0\32\14\4\0\1\14\1\0\32\14"+
    "\13\0\131\14\3\0\6\14\2\0\6\14\2\0\6\14\2\0\3\14"+
    "\3\0\2\14\3\0\2\14\22\0\3\15\4\0";

  /** 
   * Translates characters to character classes
   */
  final private static char [] yycmap = yy_unpack_cmap(yycmap_packed);

  /** 
   * Translates a state to a row index in the transition table
   */
  final private static int yy_rowMap [] = { 
        0,    55,   110,   165,   110,   220,   275,   110,   330,   110, 
      385,   440,   495,   110,   110,   110,   110,   550,   605,   660, 
      715,   770,   825,   110,   880,   935,   110,   110,   110,   110, 
      110,   110,   990,  1045,  1100,  1155,  1210,  1265,  1320,  1375, 
     1430,  1485,  1540,  1595,  1650,  1705,  1760,  1815,   385,   110, 
     1870,  1925,  1980,   110,   110,   110,   110,   110,   110,   110, 
      110,  2035,  2090,  2145,  2200,  2255,  2310,  2365,  2420,  2475, 
     2530,  2585,  2640,  2695,  2750,  2805,  2860,   495,  2915,  2970, 
     3025,  3080,   110,  3135,  3190,   385,   110,  3245,  3300,   495, 
     3355,   495,  3410,  3465,  3520,  3575,  3630,  3685,   495,  3740, 
     3795,  3850,   495,  3905,  3960,  4015,  4070,  4125,  4180,   495, 
     4235,  4290,   495,  4345,   495,   495,  4400,  4455,  4510,  4565, 
     4620,   495,  4675,   495,  4730,  4785,  4840,   495,   495,   495, 
     4895,  4950,  5005,   495,  5060,   495,  5115,   495,   495,  5170, 
     5225,   495,   495,  5280,   495
  };

  /** 
   * The packed transition table of the DFA (part 0)
   */
  final private static String yy_packed0 = 
    "\1\3\1\4\2\5\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\3\1\14\1\15\1\3\1\16\1\17\1\20"+
    "\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30"+
    "\1\31\1\32\1\33\1\34\1\35\1\36\1\37\1\40"+
    "\1\41\1\15\1\42\1\43\1\44\1\15\1\45\1\46"+
    "\1\15\1\47\1\50\2\15\1\51\3\15\1\52\1\15"+
    "\1\53\1\54\1\55\67\3\71\0\1\5\72\0\1\56"+
    "\64\0\2\7\1\56\67\0\1\57\1\60\56\0\11\61"+
    "\1\62\1\63\54\61\12\64\1\65\54\64\4\0\2\15"+
    "\6\0\2\15\23\0\26\15\23\0\1\66\66\0\1\67"+
    "\66\0\1\70\66\0\1\71\71\0\1\72\67\0\1\73"+
    "\70\0\1\74\67\0\1\75\40\0\2\15\6\0\2\15"+
    "\23\0\1\15\1\76\16\15\1\77\5\15\4\0\2\15"+
    "\6\0\2\15\23\0\7\15\1\100\3\15\1\101\12\15"+
    "\4\0\2\15\6\0\2\15\23\0\2\15\1\102\1\15"+
    "\1\103\21\15\4\0\2\15\6\0\2\15\23\0\5\15"+
    "\1\104\4\15\1\105\13\15\4\0\2\15\6\0\2\15"+
    "\23\0\12\15\1\106\13\15\4\0\2\15\6\0\2\15"+
    "\23\0\2\15\1\107\13\15\1\110\7\15\4\0\2\15"+
    "\6\0\2\15\23\0\3\15\1\111\12\15\1\112\1\113"+
    "\6\15\4\0\2\15\6\0\2\15\23\0\7\15\1\114"+
    "\16\15\4\0\2\15\6\0\2\15\23\0\2\15\1\115"+
    "\6\15\1\116\14\15\4\0\2\15\6\0\2\15\23\0"+
    "\4\15\1\117\21\15\4\0\2\15\6\0\2\15\23\0"+
    "\1\15\1\120\24\15\4\0\2\15\6\0\2\15\23\0"+
    "\1\15\1\121\24\15\4\0\2\15\6\0\2\15\23\0"+
    "\20\15\1\122\5\15\4\0\1\123\1\124\61\0\1\57"+
    "\1\4\1\5\64\57\10\60\1\125\56\60\11\61\1\126"+
    "\1\63\54\61\13\0\1\127\65\0\1\64\60\0\2\15"+
    "\6\0\2\15\23\0\2\15\1\130\23\15\4\0\2\15"+
    "\6\0\2\15\23\0\17\15\1\131\6\15\4\0\2\15"+
    "\6\0\2\15\23\0\25\15\1\132\4\0\2\15\6\0"+
    "\2\15\23\0\16\15\1\133\7\15\4\0\2\15\6\0"+
    "\2\15\23\0\10\15\1\134\15\15\4\0\2\15\6\0"+
    "\2\15\23\0\12\15\1\135\13\15\4\0\2\15\6\0"+
    "\2\15\23\0\6\15\1\136\17\15\4\0\2\15\6\0"+
    "\2\15\23\0\13\15\1\137\12\15\4\0\2\15\6\0"+
    "\2\15\23\0\15\15\1\140\10\15\4\0\2\15\6\0"+
    "\2\15\23\0\13\15\1\141\12\15\4\0\2\15\6\0"+
    "\2\15\23\0\3\15\1\142\22\15\4\0\2\15\6\0"+
    "\2\15\23\0\4\15\1\143\21\15\4\0\2\15\6\0"+
    "\2\15\23\0\1\15\1\144\24\15\4\0\2\15\6\0"+
    "\2\15\23\0\16\15\1\145\7\15\4\0\2\15\6\0"+
    "\2\15\23\0\4\15\1\146\21\15\4\0\2\15\6\0"+
    "\2\15\23\0\4\15\1\147\21\15\4\0\2\15\6\0"+
    "\2\15\23\0\12\15\1\150\13\15\4\0\2\15\6\0"+
    "\2\15\23\0\1\15\1\151\24\15\4\0\2\15\6\0"+
    "\2\15\23\0\15\15\1\152\10\15\4\0\2\15\6\0"+
    "\2\15\23\0\15\15\1\153\10\15\4\0\2\124\61\0"+
    "\7\60\1\5\1\125\56\60\4\0\2\15\6\0\2\15"+
    "\23\0\3\15\1\154\22\15\4\0\2\15\6\0\2\15"+
    "\23\0\12\15\1\155\13\15\4\0\2\15\6\0\2\15"+
    "\23\0\16\15\1\156\7\15\4\0\2\15\6\0\2\15"+
    "\23\0\13\15\1\157\12\15\4\0\2\15\6\0\2\15"+
    "\23\0\7\15\1\160\16\15\4\0\2\15\6\0\2\15"+
    "\23\0\7\15\1\161\16\15\4\0\2\15\6\0\2\15"+
    "\23\0\2\15\1\162\23\15\4\0\2\15\6\0\2\15"+
    "\23\0\14\15\1\163\11\15\4\0\2\15\6\0\2\15"+
    "\23\0\7\15\1\164\16\15\4\0\2\15\6\0\2\15"+
    "\23\0\17\15\1\165\6\15\4\0\2\15\6\0\2\15"+
    "\23\0\3\15\1\166\22\15\4\0\2\15\6\0\2\15"+
    "\23\0\13\15\1\167\12\15\4\0\2\15\6\0\2\15"+
    "\23\0\15\15\1\170\10\15\4\0\2\15\6\0\2\15"+
    "\23\0\16\15\1\171\7\15\4\0\2\15\6\0\2\15"+
    "\23\0\10\15\1\172\15\15\4\0\2\15\6\0\2\15"+
    "\23\0\16\15\1\173\7\15\4\0\2\15\6\0\2\15"+
    "\23\0\4\15\1\174\21\15\4\0\2\15\6\0\2\15"+
    "\23\0\17\15\1\175\6\15\4\0\2\15\6\0\2\15"+
    "\23\0\1\176\25\15\4\0\2\15\6\0\2\15\23\0"+
    "\10\15\1\177\15\15\4\0\2\15\6\0\2\15\23\0"+
    "\4\15\1\200\21\15\4\0\2\15\6\0\2\15\23\0"+
    "\4\15\1\201\21\15\4\0\2\15\6\0\2\15\23\0"+
    "\7\15\1\202\16\15\4\0\2\15\6\0\2\15\23\0"+
    "\12\15\1\203\13\15\4\0\2\15\6\0\2\15\23\0"+
    "\2\15\1\204\23\15\4\0\2\15\6\0\2\15\23\0"+
    "\7\15\1\205\16\15\4\0\2\15\6\0\2\15\23\0"+
    "\7\15\1\206\16\15\4\0\2\15\6\0\2\15\23\0"+
    "\1\207\25\15\4\0\2\15\6\0\2\15\23\0\4\15"+
    "\1\210\21\15\4\0\2\15\6\0\2\15\23\0\7\15"+
    "\1\211\16\15\4\0\2\15\6\0\2\15\23\0\2\15"+
    "\1\212\23\15\4\0\2\15\6\0\2\15\23\0\22\15"+
    "\1\213\3\15\4\0\2\15\6\0\2\15\23\0\17\15"+
    "\1\214\6\15\4\0\2\15\6\0\2\15\23\0\4\15"+
    "\1\215\21\15\4\0\2\15\6\0\2\15\23\0\11\15"+
    "\1\216\14\15\4\0\2\15\6\0\2\15\23\0\2\15"+
    "\1\217\23\15\4\0\2\15\6\0\2\15\23\0\7\15"+
    "\1\220\16\15\4\0\2\15\6\0\2\15\23\0\12\15"+
    "\1\221\13\15";

  /** 
   * The transition table of the DFA
   */
  final private static int yytrans [] = yy_unpack();


  /* error codes */
  final private static int YY_UNKNOWN_ERROR = 0;
  final private static int YY_ILLEGAL_STATE = 1;
  final private static int YY_NO_MATCH = 2;
  final private static int YY_PUSHBACK_2BIG = 3;

  /* error messages for the codes above */
  final private static String YY_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Internal error: unknown state",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * YY_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private final static byte YY_ATTRIBUTE[] = {
     0,  0,  9,  1,  9,  1,  1,  9,  1,  9,  1,  1,  1,  9,  9,  9, 
     9,  1,  1,  1,  1,  1,  1,  9,  1,  1,  9,  9,  9,  9,  9,  9, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  0,  0,  0, 
     0,  9,  0,  0,  0,  9,  9,  9,  9,  9,  9,  9,  9,  1,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  9,  1,  0,  1,  9,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1, 
     1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1
  };

  /** the input device */
  private java.io.Reader yy_reader;

  /** the current state of the DFA */
  private int yy_state;

  /** the current lexical state */
  private int yy_lexical_state = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char yy_buffer[] = new char[YY_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int yy_markedPos;

  /** the textposition at the last state to be included in yytext */
  private int yy_pushbackPos;

  /** the current text position in the buffer */
  private int yy_currentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int yy_startRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int yy_endRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn; 

  /** 
   * yy_atBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean yy_atBOL = true;

  /** yy_atEOF == true <=> the scanner is at the EOF */
  private boolean yy_atEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean yy_eof_done;

  /* user code: */
    public Lexer(ComplexSymbolFactory _symbolFactory, java.io.InputStream _inputStream){
		this(_inputStream);
        this.symbolFactory = _symbolFactory;
    }

	public Lexer(ComplexSymbolFactory _symbolFactory, java.io.Reader _reader){
		this(_reader);
        this.symbolFactory = _symbolFactory;
    }
    
    private StringBuffer sb;
    private ComplexSymbolFactory symbolFactory;
    private int csline,cscolumn;

    public Symbol symbol(String name, int code){
		return symbolFactory.newSymbol(name, code,
						new Location(yyline+1,yycolumn+1, yychar), // -yylength()
						new Location(yyline+1,yycolumn+yylength(), yychar+yylength())
				);
    }
    public Symbol symbol(String name, int code, String lexem){
		return symbolFactory.newSymbol(name, code, 
						new Location(yyline+1, yycolumn +1, yychar), 
						new Location(yyline+1,yycolumn+yylength(), yychar+yylength()), lexem);
    }
    
    protected void emit_warning(String message){
    		System.out.println("scanner warning: " + message + " at : 2 "+ 
    			(yyline+1) + " " + (yycolumn+1) + " " + yychar);
    }
    
    protected void emit_error(String message){
    		System.out.println("scanner error: " + message + " at : 2" + 
    			(yyline+1) + " " + (yycolumn+1) + " " + yychar);
    }


  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.yy_reader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the split, compressed DFA transition table.
   *
   * @return the unpacked transition table
   */
  private static int [] yy_unpack() {
    int [] trans = new int[5335];
    int offset = 0;
    offset = yy_unpack(yy_packed0, offset, trans);
    return trans;
  }

  /** 
   * Unpacks the compressed DFA transition table.
   *
   * @param packed   the packed transition table
   * @return         the index of the last entry
   */
  private static int yy_unpack(String packed, int offset, int [] trans) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do trans[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] yy_unpack_cmap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 2254) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception IOException  if any I/O-Error occurs
   */
  private boolean yy_refill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (yy_startRead > 0) {
      System.arraycopy(yy_buffer, yy_startRead, 
                       yy_buffer, 0, 
                       yy_endRead-yy_startRead);

      /* translate stored positions */
      yy_endRead-= yy_startRead;
      yy_currentPos-= yy_startRead;
      yy_markedPos-= yy_startRead;
      yy_pushbackPos-= yy_startRead;
      yy_startRead = 0;
    }

    /* is the buffer big enough? */
    if (yy_currentPos >= yy_buffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[yy_currentPos*2];
      System.arraycopy(yy_buffer, 0, newBuffer, 0, yy_buffer.length);
      yy_buffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = yy_reader.read(yy_buffer, yy_endRead, 
                                            yy_buffer.length-yy_endRead);

    if (numRead < 0) {
      return true;
    }
    else {
      yy_endRead+= numRead;  
      return false;
    }
  }


  /**
   * Closes the input stream.
   */
  final public void yyclose() throws java.io.IOException {
    yy_atEOF = true;            /* indicate end of file */
    yy_endRead = yy_startRead;  /* invalidate buffer    */

    if (yy_reader != null)
      yy_reader.close();
  }


  /**
   * Closes the current stream, and resets the
   * scanner to read from a new input stream.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>YY_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  final public void yyreset(java.io.Reader reader) throws java.io.IOException {
    yyclose();
    yy_reader = reader;
    yy_atBOL  = true;
    yy_atEOF  = false;
    yy_endRead = yy_startRead = 0;
    yy_currentPos = yy_markedPos = yy_pushbackPos = 0;
    yyline = yychar = yycolumn = 0;
    yy_lexical_state = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  final public int yystate() {
    return yy_lexical_state;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  final public void yybegin(int newState) {
    yy_lexical_state = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  final public String yytext() {
    return new String( yy_buffer, yy_startRead, yy_markedPos-yy_startRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  final public char yycharat(int pos) {
    return yy_buffer[yy_startRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  final public int yylength() {
    return yy_markedPos-yy_startRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void yy_ScanError(int errorCode) {
    String message;
    try {
      message = YY_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = YY_ERROR_MSG[YY_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  private void yypushback(int number)  {
    if ( number > yylength() )
      yy_ScanError(YY_PUSHBACK_2BIG);

    yy_markedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void yy_do_eof() throws java.io.IOException {
    if (!yy_eof_done) {
      yy_eof_done = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   IOException  if any I/O-Error occurs
   */
  public java_cup.runtime.Symbol next_token() throws java.io.IOException {
    int yy_input;
    int yy_action;

    // cached fields:
    int yy_currentPos_l;
    int yy_startRead_l;
    int yy_markedPos_l;
    int yy_endRead_l = yy_endRead;
    char [] yy_buffer_l = yy_buffer;
    char [] yycmap_l = yycmap;

    int [] yytrans_l = yytrans;
    int [] yy_rowMap_l = yy_rowMap;
    byte [] yy_attr_l = YY_ATTRIBUTE;

    while (true) {
      yy_markedPos_l = yy_markedPos;

      yychar+= yy_markedPos_l-yy_startRead;

      boolean yy_r = false;
      for (yy_currentPos_l = yy_startRead; yy_currentPos_l < yy_markedPos_l;
                                                             yy_currentPos_l++) {
        switch (yy_buffer_l[yy_currentPos_l]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          yy_r = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          yy_r = true;
          break;
        case '\n':
          if (yy_r)
            yy_r = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          yy_r = false;
          yycolumn++;
        }
      }

      if (yy_r) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean yy_peek;
        if (yy_markedPos_l < yy_endRead_l)
          yy_peek = yy_buffer_l[yy_markedPos_l] == '\n';
        else if (yy_atEOF)
          yy_peek = false;
        else {
          boolean eof = yy_refill();
          yy_markedPos_l = yy_markedPos;
          yy_buffer_l = yy_buffer;
          if (eof) 
            yy_peek = false;
          else 
            yy_peek = yy_buffer_l[yy_markedPos_l] == '\n';
        }
        if (yy_peek) yyline--;
      }
      yy_action = -1;

      yy_startRead_l = yy_currentPos_l = yy_currentPos = 
                       yy_startRead = yy_markedPos_l;

      yy_state = yy_lexical_state;


      yy_forAction: {
        while (true) {

          if (yy_currentPos_l < yy_endRead_l)
            yy_input = yy_buffer_l[yy_currentPos_l++];
          else if (yy_atEOF) {
            yy_input = YYEOF;
            break yy_forAction;
          }
          else {
            // store back cached positions
            yy_currentPos  = yy_currentPos_l;
            yy_markedPos   = yy_markedPos_l;
            boolean eof = yy_refill();
            // get translated positions and possibly new buffer
            yy_currentPos_l  = yy_currentPos;
            yy_markedPos_l   = yy_markedPos;
            yy_buffer_l      = yy_buffer;
            yy_endRead_l     = yy_endRead;
            if (eof) {
              yy_input = YYEOF;
              break yy_forAction;
            }
            else {
              yy_input = yy_buffer_l[yy_currentPos_l++];
            }
          }
          int yy_next = yytrans_l[ yy_rowMap_l[yy_state] + yycmap_l[yy_input] ];
          if (yy_next == -1) break yy_forAction;
          yy_state = yy_next;

          int yy_attributes = yy_attr_l[yy_state];
          if ( (yy_attributes & 1) == 1 ) {
            yy_action = yy_state; 
            yy_markedPos_l = yy_currentPos_l; 
            if ( (yy_attributes & 8) == 8 ) break yy_forAction;
          }

        }
      }

      // store back cached position
      yy_markedPos = yy_markedPos_l;

      switch (yy_action) {

        case 9: 
          {  return symbolFactory.newSymbol("Asterisque", UL_Asterisque);  }
        case 146: break;
        case 138: 
          {  return symbolFactory.newSymbol("Type Chaîne", UL_Type_Chaine);  }
        case 147: break;
        case 114: 
          {  return symbolFactory.newSymbol("Enumération", UL_Enumeration);  }
        case 148: break;
        case 102: 
          {  return symbolFactory.newSymbol("Type Entier", UL_Type_Entier);  }
        case 149: break;
        case 15: 
          {  return symbolFactory.newSymbol("Deux Points", UL_Deux_Points);  }
        case 150: break;
        case 25: 
          {  return symbolFactory.newSymbol("Esperluette", UL_Esperluette);  }
        case 151: break;
        case 54: 
          {  return symbolFactory.newSymbol("Double Egal", UL_Double_Egal);  }
        case 152: break;
        case 57: 
          {  return symbolFactory.newSymbol("Double Plus", UL_Double_Plus);  }
        case 153: break;
        case 3: 
        case 4: 
          {   }
        case 154: break;
        case 5: 
        case 6: 
          {  return symbolFactory.newSymbol("Nombre Entier", UL_Nombre_Entier, yytext());  }
        case 155: break;
        case 49: 
        case 85: 
          {  return symbolFactory.newSymbol("Chaine de caractères", UL_Chaine, yytext());  }
        case 156: break;
        case 60: 
          {  return symbolFactory.newSymbol("Double Esperluette", UL_Double_Esperluette);  }
        case 157: break;
        case 115: 
          {  return symbolFactory.newSymbol("Sinon", UL_Sinon);  }
        case 158: break;
        case 22: 
          {  return symbolFactory.newSymbol("Moins", UL_Moins);  }
        case 159: break;
        case 7: 
          {  return symbolFactory.newSymbol("Point", UL_Point);  }
        case 160: break;
        case 109: 
          {  return symbolFactory.newSymbol("Nul", UL_Nul);  }
        case 161: break;
        case 135: 
          {  return symbolFactory.newSymbol("Enregistrement", UL_Enregistrement);  }
        case 162: break;
        case 86: 
          {  return symbolFactory.newSymbol("Caractère", UL_Caractere, yytext());  }
        case 163: break;
        case 144: 
          {  return symbolFactory.newSymbol("Type Caractère", UL_Type_Caractere);  }
        case 164: break;
        case 56: 
          {  return symbolFactory.newSymbol("Supérieur Egal", UL_Superieur_Egal);  }
        case 165: break;
        case 133: 
          {  return symbolFactory.newSymbol("Tant que", UL_Tant_Que);  }
        case 166: break;
        case 127: 
          {  return symbolFactory.newSymbol("Afficher", UL_Afficher);  }
        case 167: break;
        case 123: 
          {  return symbolFactory.newSymbol("Définition Constante", UL_Definition_Constante);  }
        case 168: break;
        case 13: 
          {  return symbolFactory.newSymbol("Point Virgule", UL_Point_Virgule);  }
        case 169: break;
        case 137: 
          {  return symbolFactory.newSymbol("Retour", UL_Retour);  }
        case 170: break;
        case 91: 
          {  return symbolFactory.newSymbol("Second", UL_Second);  }
        case 171: break;
        case 121: 
          {  return symbolFactory.newSymbol("Type Vide", UL_Type_Vide);  }
        case 172: break;
        case 82: 
        case 83: 
          {  return symbolFactory.newSymbol("Nombre Flottant", UL_Nombre_Flottant, yytext());  }
        case 173: break;
        case 20: 
          {  return symbolFactory.newSymbol("Supérieur", UL_Superieur);  }
        case 174: break;
        case 19: 
          {  return symbolFactory.newSymbol("Inférieur", UL_Inferieur);  }
        case 175: break;
        case 23: 
          {  return symbolFactory.newSymbol("Pour Cent", UL_Pour_Cent);  }
        case 176: break;
        case 141: 
          {  return symbolFactory.newSymbol("Définition Type", UL_Definition_Type);  }
        case 177: break;
        case 12: 
        case 32: 
        case 33: 
        case 34: 
        case 35: 
        case 36: 
        case 37: 
        case 38: 
        case 39: 
        case 40: 
        case 41: 
        case 42: 
        case 43: 
        case 44: 
        case 61: 
        case 62: 
        case 63: 
        case 64: 
        case 65: 
        case 66: 
        case 67: 
        case 68: 
        case 69: 
        case 70: 
        case 71: 
        case 72: 
        case 73: 
        case 74: 
        case 75: 
        case 76: 
        case 78: 
        case 79: 
        case 80: 
        case 81: 
        case 87: 
        case 88: 
        case 90: 
        case 92: 
        case 93: 
        case 94: 
        case 95: 
        case 96: 
        case 97: 
        case 99: 
        case 100: 
        case 101: 
        case 103: 
        case 104: 
        case 105: 
        case 106: 
        case 107: 
        case 108: 
        case 110: 
        case 111: 
        case 113: 
        case 116: 
        case 117: 
        case 118: 
        case 119: 
        case 120: 
        case 122: 
        case 124: 
        case 125: 
        case 126: 
        case 130: 
        case 131: 
        case 132: 
        case 134: 
        case 136: 
        case 139: 
        case 140: 
        case 143: 
          {  return symbolFactory.newSymbol("Identificateur", UL_Identificateur, yytext());  }
        case 178: break;
        case 55: 
          {  return symbolFactory.newSymbol("Inférieur Egal", UL_Inferieur_Egal);  }
        case 179: break;
        case 2: 
        case 10: 
        case 11: 
        case 24: 
          {  emit_warning( "Unrecognized character '" + yytext() + "' -- ignored" );  }
        case 180: break;
        case 26: 
          {  return symbolFactory.newSymbol("Parenthese Ouvrante", UL_Parenthese_Ouvrante);  }
        case 181: break;
        case 27: 
          {  return symbolFactory.newSymbol("Parenthese Fermante", UL_Parenthese_Fermante);  }
        case 182: break;
        case 77: 
          {  return symbolFactory.newSymbol("Si", UL_Si);  }
        case 183: break;
        case 17: 
          {  return symbolFactory.newSymbol("Point d'Exclamation", UL_Point_Exclamation);  }
        case 184: break;
        case 30: 
          {  return symbolFactory.newSymbol("Crochet Ouvrant", UL_Crochet_Ouvrant);  }
        case 185: break;
        case 31: 
          {  return symbolFactory.newSymbol("Crochet Fermant", UL_Crochet_Fermant);  }
        case 186: break;
        case 98: 
          {  return symbolFactory.newSymbol("Premier", UL_Premier);  }
        case 187: break;
        case 89: 
          {  return symbolFactory.newSymbol("Nouveau", UL_Nouveau);  }
        case 188: break;
        case 14: 
          {  return symbolFactory.newSymbol("Virgule", UL_Virgule);  }
        case 189: break;
        case 8: 
          {  return symbolFactory.newSymbol("Oblique", UL_Oblique);  }
        case 190: break;
        case 28: 
          {  return symbolFactory.newSymbol("Accolade Ouvrante", UL_Accolade_Ouvrante);  }
        case 191: break;
        case 29: 
          {  return symbolFactory.newSymbol("Accolade Fermante", UL_Accolade_Fermante);  }
        case 192: break;
        case 53: 
          {  return symbolFactory.newSymbol("Exclamation Egal", UL_Exclamation_Egal);  }
        case 193: break;
        case 142: 
          {  return symbolFactory.newSymbol("Type Booléen", UL_Type_Booleen);  }
        case 194: break;
        case 128: 
          {  return symbolFactory.newSymbol("Type Entier", UL_Type_Flottant);  }
        case 195: break;
        case 129: 
          {  return symbolFactory.newSymbol("Faux", UL_Faux);  }
        case 196: break;
        case 112: 
          {  return symbolFactory.newSymbol("Vrai", UL_Vrai);  }
        case 197: break;
        case 21: 
          {  return symbolFactory.newSymbol("Plus", UL_Plus);  }
        case 198: break;
        case 18: 
          {  return symbolFactory.newSymbol("Egal", UL_Egal);  }
        case 199: break;
        case 16: 
          {  return symbolFactory.newSymbol("Point d'Interrogation", UL_Point_Interrogation);  }
        case 200: break;
        case 58: 
          {  return symbolFactory.newSymbol("Double Moins", UL_Double_Moins);  }
        case 201: break;
        case 59: 
          {  return symbolFactory.newSymbol("Double Barre", UL_Double_Barre);  }
        case 202: break;
        default: 
          if (yy_input == YYEOF && yy_startRead == yy_currentPos) {
            yy_atEOF = true;
            yy_do_eof();
              {     return symbolFactory.newSymbol("EOF",sym.EOF);
 }
          } 
          else {
            yy_ScanError(YY_NO_MATCH);
          }
      }
    }
  }


}
