using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using RestApi.Models;

// For more information on enabling Web API for empty projects, visit https://go.microsoft.com/fwlink/?LinkID=397860

namespace RestApi.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class RestController : Controller
    {
        private readonly RestContext _context;

        public RestController(RestContext context)
        {
            _context = context;

            if(_context.RestItems.Count() == 0)
            {
                //Create a new RestItem if collection is empty,
                //which means you can't delete all RestItems
                _context.RestItems.Add(new RestItem { Name = "Item1" });
                _context.RestCustomers.Add(new RestCustomer { RSA = "Item1" });
                _context.SaveChanges();
            }
        }

        // GET: api/<controller>
       /* [HttpGet]
        public IEnumerable<string> Get()
        {
            return new string[] { "value1", "value2" };
        }

        // GET api/<controller>/5
        [HttpGet("{id}")]
        public string Get(int id)
        {
            return "value";
        }*/

        [HttpGet]
        public ActionResult<List<RestItem>> GetAll()
        {
            return _context.RestItems.ToList();
        }

        [HttpGet("{id}", Name = "GetRest")]
        public ActionResult<RestItem> GetById(long id)
        {
            var item = _context.RestItems.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            return item;
        }

        [HttpGet("customer/{id}", Name = "GetCustomer")]
        public ActionResult<RestCustomer> GetCustomerById(long id)
        {
            var item = _context.RestCustomers.Find(id);
            if (item == null)
            {
                return NotFound();
            }
            return item;
        }

        [HttpGet("customer")]
        public ActionResult<List<RestCustomer>> GetCustomers()
        {
            return _context.RestCustomers.ToList();
        }

        // POST api/<controller>
        [HttpPost]
        public IActionResult Create(RestItem rest)
        {
            _context.RestItems.Add(rest);
            _context.SaveChanges();

            return CreatedAtRoute("GetRest", new { id = rest.Id }, rest);
        }


        [HttpPost("customer")]
        public IActionResult CreateCustomer(RestCustomer rest)
        {
            _context.RestCustomers.Add(rest);
            _context.SaveChanges();

            return CreatedAtRoute("GetCustomer", new { id = rest.Id }, rest);
        }

        // PUT api/<controller>/5
        [HttpPut("{id}")]
        public IActionResult Update(long id, RestItem item)
        {
            var rest = _context.RestItems.Find(id);
            if(rest == null)
            {
                return NotFound();
            }

            rest.IsComplete = item.IsComplete;
            rest.Name = item.Name;

            _context.RestItems.Update(rest);
            _context.SaveChanges();
            return NoContent();
        }

        // DELETE api/<controller>/5
        [HttpDelete("{id}")]
        public IActionResult Delete(long id)
        {
            var rest = _context.RestItems.Find(id);
            if (rest == null)
            {
                return NotFound();
            }
            

            _context.RestItems.Remove(rest);
            _context.SaveChanges();
            return NoContent();
        }
    }

   
}
