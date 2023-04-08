package com.quanht.config;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Stream;

public class CustomGenerateId implements IdentifierGenerator {
    private String prefix;

    @Override
    public Serializable generate(
            SharedSessionContractImplementor session, Object obj)
            throws HibernateException {
//        String query = String.format("select %s from %s",
//
//                session.getEntityPersister(obj.getClass().getName(), obj)
//
//                        .getIdentifierPropertyName(),
//
//                obj.getClass().getSimpleName());
//
//        Stream<String> ids = session.createQuery(query, String.class).stream();
//
//
//        Long max = ids.map(o -> o.replace(prefix + "-", ""))
//
//                .mapToLong(Long::parseLong)
//
//                .max()
//
//                .orElse(0L);
//
//
//        return prefix + "-" + (max + 1);

        // Get the current date as a string without hyphens
        String currentDate = LocalDate.now().toString().replace("-", "");

        // Generate 4 random characters/numbers
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int rand = random.nextInt(36);
            char c = (rand < 10) ? (char) ('0' + rand) : (char) ('a' + rand - 10);
            randomString.append(c);
        }

        // Combine the current date and random string, then convert to uppercase
        return prefix + (currentDate + randomString).toUpperCase();
    }

    @Override
    public void configure(Type type, Properties properties,
                          ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }

}
