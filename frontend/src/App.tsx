import {useState} from 'react'

function App() {
  const [isModalOpen, setIsModalOpen] = useState(false);

  return (
      <>
      <div>
        <h1>Hello Daneotong!</h1>
      </div>
      <div>
          <input className={"border border-gray-300 rounded px-100 py-2"} type={"text"}/>
          <button className="-bg-conic-30" onClick={() => (setIsModalOpen(true))}>입력</button>
      </div>
      {isModalOpen && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center">
          <div className="bg-white p-4 rounded shadow-lg">
            <input className={"border border-gray-300 rounded px-100 py-2"} type={"text"}/>
            <button onClick={() => setIsModalOpen(false)}>Close</button>
            <button onClick={() => false}>Confirm</button>
          </div>
        </div>
      )}
      </>
  )
}

export default App
