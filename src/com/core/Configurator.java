package com.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

class Configurator {
    String _mainEntity;
    String _manyToManyPivot;
    String _secondaryEntity;
    Integer _numAttrsEntities;
    Vector<String> _attrsMain;
    Vector<String> _entities;
    Vector<String> _attrsEntities;
    Vector<String> _pivotAttrs;
    ArrayList<Vector<String>> _table;

    Configurator(String mainEntity,
                 String manyToManyPivot,
                 String secondaryEntity,
                 Integer numAttrsEntities,
                 String[] attrsMain,
                 String[] entities,
                 String[] attrsEntities,
                 String[] pivotAttrs,
                 ArrayList<Vector<String>> t) {
        _mainEntity       = mainEntity;
        _manyToManyPivot  = manyToManyPivot;
        _secondaryEntity  = secondaryEntity;
        _numAttrsEntities = numAttrsEntities;
        _attrsMain        = new Vector<>(Arrays.asList(attrsMain));
        _entities         = new Vector<>(Arrays.asList(entities));
        _attrsEntities    = new Vector<>(Arrays.asList(attrsEntities));
        _pivotAttrs       = new Vector<>(Arrays.asList(pivotAttrs));
        _table            = t;
    }
}
