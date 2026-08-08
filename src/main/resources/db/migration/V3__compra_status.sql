-- Status de compra (cancelamento lógico) — parcelas canceladas via enum StatusParcela

ALTER TABLE compra ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ATIVA';
