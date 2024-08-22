import Control.Concurrent
import Control.Concurrent.STM
import Control.Concurrent.STM.TVar
import Control.Monad.IO.Class (liftIO)

type Buffer a = TVar [a]

newBuffer :: [a] -> IO (Buffer a)
newBuffer = newTVarIO

put :: Buffer a -> a -> STM ()
put buffer item = do
    items <- readTVar buffer
    writeTVar buffer (items ++ [item])

get :: Buffer a -> STM a
get buffer = do
    xs <- readTVar buffer
    case xs of
        [] -> retry
        (y:ys) -> do    
            writeTVar buffer ys
            return y

main :: IO ()
main = do
    buffer <- newBuffer []
    atomically $ do
        put buffer 1
        put buffer 2
    
    item1 <- atomically $ get buffer
    putStrLn $ "Peguei: " ++ show item1
 
    item2 <- atomically $ get buffer
    putStrLn $ "Peguei: " ++ show item2
