/* Generated By:JJTree: Do not edit this line. ASTComponentDecl.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package wyvern.tools.parsing.coreparser.arch;

public
class ASTComponentDecl extends SimpleNode {
  public ASTComponentDecl(int id) {
    super(id);
  }

  public ASTComponentDecl(ArchParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ArchParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=93f39b2437130e2f040b4c0a9133a630 (do not edit this line) */
