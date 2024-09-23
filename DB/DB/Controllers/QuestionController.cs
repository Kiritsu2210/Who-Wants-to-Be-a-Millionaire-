using Microsoft.AspNetCore.Mvc;
using DB.Models;
using System.Collections.Generic;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;

namespace DB.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class QuestionsController : ControllerBase
    {
        private readonly ApplicationDbContext _context;

        public QuestionsController(ApplicationDbContext context)
        {
            _context = context;
        }

        [HttpGet]
        public async Task<ActionResult<IEnumerable<Question>>> GetQuestions()
        {
            return await _context.Questions.ToListAsync();
        }

        [HttpGet("{Nhom}")]
        public async Task<ActionResult<Question>> GetQuestion(int Nhom)
        {
            var questions = await _context.Questions
                                   .Where(q => q.Nhom == Nhom)
                                   .ToListAsync();

            var random = new Random();
            var randomQuestion = questions[random.Next(questions.Count)];

            return Ok(randomQuestion);
        }

        [HttpPost]
        [Route("Create")]

        public async Task<ActionResult<Question>> Create(Question q)
        {
            var question = new Question();
            question.ID = q.ID;
            question.Nhom = q.Nhom;
            question.NoiDung = q.NoiDung;
            question.DapAnDung = q.DapAnDung;
            question.DapAnSai1 = q.DapAnSai1;
            question.DapAnSai2 = q.DapAnSai2;
            question.DapAnSai3 = q.DapAnSai3;
            await _context.Questions.AddAsync(question);
            _context.SaveChanges();
            return Ok(question);
        }
    }
}
