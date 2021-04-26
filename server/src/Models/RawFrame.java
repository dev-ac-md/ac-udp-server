package Models;

import java.io.Serializable;

public class RawFrame implements Serializable {
    public long m_lProto;
    public short m_uType;
    public long m_lStamp;
    public short m_PeerId;
    public char[] m_bData;
}
