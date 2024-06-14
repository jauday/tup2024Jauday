package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.exception.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.exception.CuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
public class CuentaService {
    CuentaDao cuentaDao = new CuentaDao();

    @Autowired
    ClienteService clienteService;

    private static final List<String> CUENTAS_SOPORTADAS = Arrays.asList("CAJA_AHORRO_PESOS", "CUENTA_CORRIENTE_PESOS", "CAJA_AHORRO_DOLARES");

    public void darDeAltaCuenta(Cuenta cuenta, long dniTitular) throws CuentaAlreadyExistsException, TipoCuentaAlreadyExistsException, CuentaNoSoportadaException {
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }

        if (!tipoDeCuentaSoportada(cuenta)) {
            throw new CuentaNoSoportadaException("La cuenta con tipo " + cuenta.getTipoCuenta() + " y moneda " + cuenta.getMoneda() + " no es soportada.");
        }

        clienteService.agregarCuenta(cuenta, dniTitular);
        cuentaDao.save(cuenta);
    }

    public boolean tipoDeCuentaSoportada(Cuenta cuenta) {
        String cuentaKey = cuenta.getTipoCuenta().name() + "_" + cuenta.getMoneda().name();
        return CUENTAS_SOPORTADAS.contains(cuentaKey);
    }

    public Cuenta find(long id) {
        return cuentaDao.find(id);
    }
}