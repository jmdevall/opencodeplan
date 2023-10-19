package jmdevall.opencodeplan.domain.dependencygraph;


public enum Label{
    
    PARENT_OF,
    CHILD_OF,

    BASE_CLASS_OF,
    DERIVED_CLASS_OF,

    IMPORTS,
    IMPORTED_BY,

    USES,
    USED_BY,

    OVERRIDES,
    OVERRIDEN_BY,

    INSTANTIATES,
    INSTANTIATED_BY,

    CALLS,
    CALLED_BY,

    CONSTRUCTS,
    CONSTRUCTED_BY

}