import { toast } from 'react-toastify'

export function showInfoToast (message) {
  toast.info(message)
}

export function showSuccessToast (message) {
  toast.success(`🎉 ${message}`)
}

export function showErrorToast (message) {
  toast.error(`❌ ${message}`)
}
