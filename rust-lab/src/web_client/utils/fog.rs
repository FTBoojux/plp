pub mod fog{
    use std::sync::{mpsc, Mutex};
    use std::thread;

    static SENDER: Mutex<Option<mpsc::Sender<String>>> = Mutex::new(None);
    pub fn init(){
        let mut sender_guard = SENDER.lock().unwrap();
        if sender_guard.is_none() {
            let (sender, receiver) = mpsc::channel();
            thread::spawn(move||{
                while let Ok(message) = receiver.recv() {
                    println!("{}", message);
                }
            });
            *sender_guard = Some(sender);
        }
    }

    pub fn log(message:impl Into<String>){
        if let Ok(guard) = SENDER.lock(){
            if let Some(sender) = &*guard{
                let _ = sender.send(message.into());
            }
        }
    }
}