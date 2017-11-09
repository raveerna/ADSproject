import java.io.*;
class Node
{
   
    int datafield; // frequency
    Node childpointer; //child node
    Node leftpointer; //left node
    Node parentpointer; // parent node
    Node rightpointer; // right node
    Node temporary; // temporary node
    Node x;
    Node y;
    boolean ismark; // childcut value
    int degreefield; // degree of node
public String hashtags; // hashtag
    public Node(String hashtags,int datafield) //constructor of Node class
    {
        rightpointer = this;
        leftpointer = this;
        this.datafield = datafield;
        this.hashtags= hashtags;
    }

    public final String getHashtag() // returns string hashtags (hashtag)
    {
        return hashtags;
    }
    public final int getData() // returns integer datafield (frequency)
    {
        return datafield;
    }
}


