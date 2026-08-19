// Small helper: highlight active nav link based on current path
(function(){
  const path = location.pathname.split('/').pop() || 'index.html';
  document.querySelectorAll('a[data-nav]').forEach(a=>{
    const target = a.getAttribute('href');
    if(target === path) a.classList.add('active');
  });
})();

// Handle static-site forms that still point to the Formspree placeholder.
// This lets the Neshtek site work without requiring a Formspree form ID.
(function(){
  document.querySelectorAll('form[action*="formspree.io/f/YOUR_FORM_ID"]').forEach(function(form){
    form.addEventListener('submit', function(e){
      e.preventDefault();
      const data = new FormData(form);
      const product = data.get('product') || 'Neshtek Website';
      const email = data.get('email') || '';
      const monitor = data.get('monitor_type') || '';
      const subject = encodeURIComponent(product + ' - Early Access Request');
      const body = encodeURIComponent(
        'Product: ' + product + '\n' +
        'Work email: ' + email + '\n' +
        'Monitoring requirement: ' + monitor + '\n\n' +
        'Please contact me regarding early access.'
      );
      window.location.href = 'mailto:hello@neshtek.com?subject=' + subject + '&body=' + body;
    });
  });
})();
