package jmdevall.opencodeplan;

public class ReplaceSpec{
	   public int begin;
	   public int end;
	   String toReplace;
	   
	   public ReplaceSpec(int begin, int end, String toReplace){
	      this.begin=begin;
	      this.end=end;
	      this.toReplace=toReplace;
	   }
	}
