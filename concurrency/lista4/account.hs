import Control.Concurrent
import Control.Concurrent.STM
import Control.Monad(when)

newtype Conta = Conta (TVar Int)

novaConta :: Int -> IO Conta
novaConta saldoInicial = do 
    tvar <- newTVarIO saldoInicial
    return (Conta tvar)

-- a)
saque :: Conta -> Int -> STM ()
saque (Conta tvar) quantia = do
    saldo <- readTVar tvar
    writeTVar tvar (saldo - quantia)

-- b)
deposito :: Conta -> Int -> STM ()
deposito conta quantia = saque conta (-quantia)

-- c)
saque2 :: Conta -> Int -> STM ()
saque2 (Conta tvar) quantia = do
    saldo <- readTVar tvar 
    when (saldo < quantia) retry
    writeTVar tvar (saldo - quantia)

saldo :: Conta -> STM Int
saldo (Conta tvar) = readTVar tvar

main :: IO ()
main = do
    conta <- novaConta 100

    saldoInicial <- atomically $ saldo conta
    putStrLn $ "Saldo inicial: " ++ show saldoInicial

    atomically $ saque2 conta 50
    saldoPosSaque <- atomically $ saldo conta
    putStrLn $ "Saldo após saque de 50: " ++ show saldoPosSaque

    atomically $ deposito conta 30
    saldoPosDeposito <- atomically $ saldo conta
    putStrLn $ "Saldo após depósito de 30: " ++ show saldoPosDeposito

    atomically $ saque2 conta 100
    saldoFinal <- atomically $ saldo conta
    putStrLn $ "Saldo final: " ++ show saldoFinal