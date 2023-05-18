package com.jasonvillar.works.register.configtests;

import org.junit.jupiter.api.ClassDescriptor;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

class SpringBootTestClassOrderer implements ClassOrderer {
    @Override
    public void orderClasses(ClassOrdererContext classOrdererContext) {
        classOrdererContext.getClassDescriptors().sort(Comparator.comparingInt(SpringBootTestClassOrderer::getOrder));
    }

    private static int getOrder(ClassDescriptor classDescriptor) {
        String className = classDescriptor.getDisplayName();
        if (className.endsWith("ControllerTest")) {
            return 5;
        } else if (className.endsWith("ServiceTest")) {
            return 4;
        } else if (className.endsWith("RepositoryTest")) {
            return 3;
        } else if (className.endsWith("ConfigTest")) {
            return 2;
        } else {
            return 1;
        }
    }
}
