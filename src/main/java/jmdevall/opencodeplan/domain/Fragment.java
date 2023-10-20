package jmdevall.opencodeplan.domain;

public class Fragment {

	/**
	Simply extracting
	code of the block 𝐵 loses information about relationship of 𝐵 with the surrounding code.
	Keeping the entire file on the other hand takes up prompt space and is often unnecessary.
	We found the surrounding context is most helpful when a block belongs to a class. For such
	blocks, we sketch the enclosing class. That is, in addition to the code of block 𝐵, we also
	keep declarations of the enclosing class and its members. As we discuss later, this sketched
	representation also helps us merge the LLM’s output into a source code file more easily.
	*/
}