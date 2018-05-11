/**
 * Autogenerated by Thrift Compiler (0.11.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.ccfea.tickdata.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.11.0)", date = "2018-04-14")
public class DataFrame implements org.apache.thrift.TBase<DataFrame, DataFrame._Fields>, java.io.Serializable, Cloneable, Comparable<DataFrame> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DataFrame");

  private static final org.apache.thrift.protocol.TField TIME_STAMPS_FIELD_DESC = new org.apache.thrift.protocol.TField("timeStamps", org.apache.thrift.protocol.TType.LIST, (short)1);
  private static final org.apache.thrift.protocol.TField COLUMNS_FIELD_DESC = new org.apache.thrift.protocol.TField("columns", org.apache.thrift.protocol.TType.MAP, (short)2);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new DataFrameStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new DataFrameTupleSchemeFactory();

  public java.util.List<java.lang.Long> timeStamps; // required
  public java.util.Map<java.lang.String,java.util.List<java.lang.Double>> columns; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    TIME_STAMPS((short)1, "timeStamps"),
    COLUMNS((short)2, "columns");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // TIME_STAMPS
          return TIME_STAMPS;
        case 2: // COLUMNS
          return COLUMNS;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TIME_STAMPS, new org.apache.thrift.meta_data.FieldMetaData("timeStamps", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64))));
    tmpMap.put(_Fields.COLUMNS, new org.apache.thrift.meta_data.FieldMetaData("columns", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.MapMetaData(org.apache.thrift.protocol.TType.MAP, 
            new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING), 
            new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
                new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)))));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DataFrame.class, metaDataMap);
  }

  public DataFrame() {
  }

  public DataFrame(
    java.util.List<java.lang.Long> timeStamps,
    java.util.Map<java.lang.String,java.util.List<java.lang.Double>> columns)
  {
    this();
    this.timeStamps = timeStamps;
    this.columns = columns;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DataFrame(DataFrame other) {
    if (other.isSetTimeStamps()) {
      java.util.List<java.lang.Long> __this__timeStamps = new java.util.ArrayList<java.lang.Long>(other.timeStamps);
      this.timeStamps = __this__timeStamps;
    }
    if (other.isSetColumns()) {
      java.util.Map<java.lang.String,java.util.List<java.lang.Double>> __this__columns = new java.util.HashMap<java.lang.String,java.util.List<java.lang.Double>>(other.columns.size());
      for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.Double>> other_element : other.columns.entrySet()) {

        java.lang.String other_element_key = other_element.getKey();
        java.util.List<java.lang.Double> other_element_value = other_element.getValue();

        java.lang.String __this__columns_copy_key = other_element_key;

        java.util.List<java.lang.Double> __this__columns_copy_value = new java.util.ArrayList<java.lang.Double>(other_element_value);

        __this__columns.put(__this__columns_copy_key, __this__columns_copy_value);
      }
      this.columns = __this__columns;
    }
  }

  public DataFrame deepCopy() {
    return new DataFrame(this);
  }

  @Override
  public void clear() {
    this.timeStamps = null;
    this.columns = null;
  }

  public int getTimeStampsSize() {
    return (this.timeStamps == null) ? 0 : this.timeStamps.size();
  }

  public java.util.Iterator<java.lang.Long> getTimeStampsIterator() {
    return (this.timeStamps == null) ? null : this.timeStamps.iterator();
  }

  public void addToTimeStamps(long elem) {
    if (this.timeStamps == null) {
      this.timeStamps = new java.util.ArrayList<java.lang.Long>();
    }
    this.timeStamps.add(elem);
  }

  public java.util.List<java.lang.Long> getTimeStamps() {
    return this.timeStamps;
  }

  public DataFrame setTimeStamps(java.util.List<java.lang.Long> timeStamps) {
    this.timeStamps = timeStamps;
    return this;
  }

  public void unsetTimeStamps() {
    this.timeStamps = null;
  }

  /** Returns true if field timeStamps is set (has been assigned a value) and false otherwise */
  public boolean isSetTimeStamps() {
    return this.timeStamps != null;
  }

  public void setTimeStampsIsSet(boolean value) {
    if (!value) {
      this.timeStamps = null;
    }
  }

  public int getColumnsSize() {
    return (this.columns == null) ? 0 : this.columns.size();
  }

  public void putToColumns(java.lang.String key, java.util.List<java.lang.Double> val) {
    if (this.columns == null) {
      this.columns = new java.util.HashMap<java.lang.String,java.util.List<java.lang.Double>>();
    }
    this.columns.put(key, val);
  }

  public java.util.Map<java.lang.String,java.util.List<java.lang.Double>> getColumns() {
    return this.columns;
  }

  public DataFrame setColumns(java.util.Map<java.lang.String,java.util.List<java.lang.Double>> columns) {
    this.columns = columns;
    return this;
  }

  public void unsetColumns() {
    this.columns = null;
  }

  /** Returns true if field columns is set (has been assigned a value) and false otherwise */
  public boolean isSetColumns() {
    return this.columns != null;
  }

  public void setColumnsIsSet(boolean value) {
    if (!value) {
      this.columns = null;
    }
  }

  public void setFieldValue(_Fields field, java.lang.Object value) {
    switch (field) {
    case TIME_STAMPS:
      if (value == null) {
        unsetTimeStamps();
      } else {
        setTimeStamps((java.util.List<java.lang.Long>)value);
      }
      break;

    case COLUMNS:
      if (value == null) {
        unsetColumns();
      } else {
        setColumns((java.util.Map<java.lang.String,java.util.List<java.lang.Double>>)value);
      }
      break;

    }
  }

  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case TIME_STAMPS:
      return getTimeStamps();

    case COLUMNS:
      return getColumns();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case TIME_STAMPS:
      return isSetTimeStamps();
    case COLUMNS:
      return isSetColumns();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof DataFrame)
      return this.equals((DataFrame)that);
    return false;
  }

  public boolean equals(DataFrame that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_timeStamps = true && this.isSetTimeStamps();
    boolean that_present_timeStamps = true && that.isSetTimeStamps();
    if (this_present_timeStamps || that_present_timeStamps) {
      if (!(this_present_timeStamps && that_present_timeStamps))
        return false;
      if (!this.timeStamps.equals(that.timeStamps))
        return false;
    }

    boolean this_present_columns = true && this.isSetColumns();
    boolean that_present_columns = true && that.isSetColumns();
    if (this_present_columns || that_present_columns) {
      if (!(this_present_columns && that_present_columns))
        return false;
      if (!this.columns.equals(that.columns))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetTimeStamps()) ? 131071 : 524287);
    if (isSetTimeStamps())
      hashCode = hashCode * 8191 + timeStamps.hashCode();

    hashCode = hashCode * 8191 + ((isSetColumns()) ? 131071 : 524287);
    if (isSetColumns())
      hashCode = hashCode * 8191 + columns.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(DataFrame other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetTimeStamps()).compareTo(other.isSetTimeStamps());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTimeStamps()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.timeStamps, other.timeStamps);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.valueOf(isSetColumns()).compareTo(other.isSetColumns());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetColumns()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.columns, other.columns);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("DataFrame(");
    boolean first = true;

    sb.append("timeStamps:");
    if (this.timeStamps == null) {
      sb.append("null");
    } else {
      sb.append(this.timeStamps);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("columns:");
    if (this.columns == null) {
      sb.append("null");
    } else {
      sb.append(this.columns);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class DataFrameStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DataFrameStandardScheme getScheme() {
      return new DataFrameStandardScheme();
    }
  }

  private static class DataFrameStandardScheme extends org.apache.thrift.scheme.StandardScheme<DataFrame> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DataFrame struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TIME_STAMPS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list0 = iprot.readListBegin();
                struct.timeStamps = new java.util.ArrayList<java.lang.Long>(_list0.size);
                long _elem1;
                for (int _i2 = 0; _i2 < _list0.size; ++_i2)
                {
                  _elem1 = iprot.readI64();
                  struct.timeStamps.add(_elem1);
                }
                iprot.readListEnd();
              }
              struct.setTimeStampsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // COLUMNS
            if (schemeField.type == org.apache.thrift.protocol.TType.MAP) {
              {
                org.apache.thrift.protocol.TMap _map3 = iprot.readMapBegin();
                struct.columns = new java.util.HashMap<java.lang.String,java.util.List<java.lang.Double>>(2*_map3.size);
                java.lang.String _key4;
                java.util.List<java.lang.Double> _val5;
                for (int _i6 = 0; _i6 < _map3.size; ++_i6)
                {
                  _key4 = iprot.readString();
                  {
                    org.apache.thrift.protocol.TList _list7 = iprot.readListBegin();
                    _val5 = new java.util.ArrayList<java.lang.Double>(_list7.size);
                    double _elem8;
                    for (int _i9 = 0; _i9 < _list7.size; ++_i9)
                    {
                      _elem8 = iprot.readDouble();
                      _val5.add(_elem8);
                    }
                    iprot.readListEnd();
                  }
                  struct.columns.put(_key4, _val5);
                }
                iprot.readMapEnd();
              }
              struct.setColumnsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, DataFrame struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.timeStamps != null) {
        oprot.writeFieldBegin(TIME_STAMPS_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I64, struct.timeStamps.size()));
          for (long _iter10 : struct.timeStamps)
          {
            oprot.writeI64(_iter10);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.columns != null) {
        oprot.writeFieldBegin(COLUMNS_FIELD_DESC);
        {
          oprot.writeMapBegin(new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.LIST, struct.columns.size()));
          for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.Double>> _iter11 : struct.columns.entrySet())
          {
            oprot.writeString(_iter11.getKey());
            {
              oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.DOUBLE, _iter11.getValue().size()));
              for (double _iter12 : _iter11.getValue())
              {
                oprot.writeDouble(_iter12);
              }
              oprot.writeListEnd();
            }
          }
          oprot.writeMapEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DataFrameTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public DataFrameTupleScheme getScheme() {
      return new DataFrameTupleScheme();
    }
  }

  private static class DataFrameTupleScheme extends org.apache.thrift.scheme.TupleScheme<DataFrame> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DataFrame struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetTimeStamps()) {
        optionals.set(0);
      }
      if (struct.isSetColumns()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetTimeStamps()) {
        {
          oprot.writeI32(struct.timeStamps.size());
          for (long _iter13 : struct.timeStamps)
          {
            oprot.writeI64(_iter13);
          }
        }
      }
      if (struct.isSetColumns()) {
        {
          oprot.writeI32(struct.columns.size());
          for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.Double>> _iter14 : struct.columns.entrySet())
          {
            oprot.writeString(_iter14.getKey());
            {
              oprot.writeI32(_iter14.getValue().size());
              for (double _iter15 : _iter14.getValue())
              {
                oprot.writeDouble(_iter15);
              }
            }
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DataFrame struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list16 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.I64, iprot.readI32());
          struct.timeStamps = new java.util.ArrayList<java.lang.Long>(_list16.size);
          long _elem17;
          for (int _i18 = 0; _i18 < _list16.size; ++_i18)
          {
            _elem17 = iprot.readI64();
            struct.timeStamps.add(_elem17);
          }
        }
        struct.setTimeStampsIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TMap _map19 = new org.apache.thrift.protocol.TMap(org.apache.thrift.protocol.TType.STRING, org.apache.thrift.protocol.TType.LIST, iprot.readI32());
          struct.columns = new java.util.HashMap<java.lang.String,java.util.List<java.lang.Double>>(2*_map19.size);
          java.lang.String _key20;
          java.util.List<java.lang.Double> _val21;
          for (int _i22 = 0; _i22 < _map19.size; ++_i22)
          {
            _key20 = iprot.readString();
            {
              org.apache.thrift.protocol.TList _list23 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.DOUBLE, iprot.readI32());
              _val21 = new java.util.ArrayList<java.lang.Double>(_list23.size);
              double _elem24;
              for (int _i25 = 0; _i25 < _list23.size; ++_i25)
              {
                _elem24 = iprot.readDouble();
                _val21.add(_elem24);
              }
            }
            struct.columns.put(_key20, _val21);
          }
        }
        struct.setColumnsIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
