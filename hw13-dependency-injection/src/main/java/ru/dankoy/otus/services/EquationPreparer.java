package ru.dankoy.otus.services;

import ru.dankoy.otus.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
