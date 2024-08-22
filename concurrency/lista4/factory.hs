import Control.Concurrent
import Control.Monad (forever, replicateM)

data Porca = Porca deriving (Show)
data Parafuso = Parafuso deriving (Show)
data Par = Par Porca Parafuso deriving (Show)

producaoPorca :: MVar Porca -> IO ()
producaoPorca caixaPorcas = forever $ do
    putStrLn "Produzindo uma porca..."
    threadDelay 500000
    putMVar caixaPorcas Porca
    putStrLn "Porca colocada na caixa."

producaoParafuso :: MVar Parafuso -> IO ()
producaoParafuso caixaParafusos = forever $ do
    putStrLn "Produzindo um parafuso..."
    threadDelay 700000
    putMVar caixaParafusos Parafuso
    putStrLn "Parafuso colocado na caixa."

montagemPar :: MVar Porca -> MVar Parafuso -> MVar Par -> IO ()
montagemPar caixaPorcas caixaParafusos caixaPares = forever $ do
    putStrLn "Montando um par..."
    porca <- takeMVar caixaPorcas
    parafuso <- takeMVar caixaParafusos
    let par = Par porca parafuso
    putMVar caixaPares par 
    putStrLn $ "Par montado e colocado na caixa." ++ show par

main :: IO ()
main = do
    caixaPorcas <- newEmptyMVar
    caixaParafusos <- newEmptyMVar
    caixaPares <- newEmptyMVar

    forkIO $ producaoPorca caixaPorcas
    forkIO $ producaoParafuso caixaParafusos
    forkIO $ montagemPar caixaPorcas caixaParafusos caixaPares

    threadDelay 10000000  


    pares <- replicateM 10 (takeMVar caixaPares)
    putStrLn $ "Pares montados: " ++ show pares