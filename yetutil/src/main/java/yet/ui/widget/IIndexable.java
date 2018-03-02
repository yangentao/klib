package yet.ui.widget;

//要索引的集合必须是按tag经过排序的
public interface IIndexable {

    //返回 #, A, B, C等, 不能返回null
    char getIndexTag();
}
