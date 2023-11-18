import {createContext, useState} from "react";

export const AppContext = createContext();

export default function AppContextProvider({ children }) {

  const [refreshTrigger, updateRefreshTrigger] = useState(0);
  const refresh = () => {
    updateRefreshTrigger(prev => prev + 1);
  }

  const [loading, setLoading] = useState(false);

  const appContext = {
    refreshTrigger, refresh,
    loading, setLoading,
  };

  return (
    <AppContext.Provider value={appContext}>
      {children}
    </AppContext.Provider>
  )
}
